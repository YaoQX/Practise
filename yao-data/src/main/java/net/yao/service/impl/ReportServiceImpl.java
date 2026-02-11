package net.yao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.yao.config.KafkaTopicConfig;
import net.yao.dto.ReportDTO;
import net.yao.enums.ReportStateEnum;
import net.yao.exception.BizException;
import net.yao.mapper.ReportDetailStressMapper;
import net.yao.mapper.ReportMapper;
import net.yao.model.ReportDO;
import net.yao.model.ReportDetailStressDO;
import net.yao.req.ReportSaveReq;
import net.yao.req.ReportUpdateReq;
import net.yao.service.ReportService;
import net.yao.util.JsonUtil;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import net.yao.enums.BizCodeEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private ReportDetailStressMapper reportDetailStressMapper;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    public ReportDTO save(ReportSaveReq req) {
        // 1. 【核心：幂等检查】防止 8082 重试导致的重复插入
        LambdaQueryWrapper<ReportDO> query = new LambdaQueryWrapper<>();
        query.eq(ReportDO::getProjectId, req.getProjectId())
                .eq(ReportDO::getCaseId, req.getCaseId())
                .eq(ReportDO::getExecuteState, "EXECUTING"); // 或者用 ReportStateEnum.EXECUTING.name()

        // 尝试查一下有没有正在跑的报告
        ReportDO existingReport = reportMapper.selectOne(query);

        if (existingReport != null) {
            log.warn("Duplicate stress test request detected; report already exists：{}", existingReport.getId());
            // 如果你希望 8082 拿到之前的报告继续跑，就直接返回旧的
            return ReportDTO.builder()
                    .id(existingReport.getId())
                    .projectId(existingReport.getProjectId())
                    .name(existingReport.getName())
                    .build();

            // 或者：如果你希望 8082 直接报错并停止，就抛出你刚才定义的枚举
            // throw new BizException(BizCodeEnum.STRESS_REPORT_EXISTING);
        }

        // 2. 【原有插入逻辑】走到这里说明是全新的请求
        ReportDO reportDO = SpringBeanUtil.copyProperties(req, ReportDO.class);
        reportMapper.insert(reportDO);

        // 3. 【返回结果】使用你习惯的 Builder 模式
        return ReportDTO.builder()
                .id(reportDO.getId())
                .projectId(reportDO.getProjectId())
                .name(reportDO.getName())
                .build();

    }

    public void updateReportState(ReportUpdateReq req) {

        ReportDTO reportDTO = ReportDTO.builder().id(req.getId()).executeState(req.getExecuteState()).endTime(req.getEndTime()).build();

        ReportDO reportDO = reportMapper.selectById(reportDTO.getId());
        if (reportDO == null) {
            return;
        }

        //代码首先查出当前数据库中该测试报告的最新一条明细数据（oldReportDetailStressDO）
        LambdaQueryWrapper<ReportDetailStressDO> queryWrapper = new LambdaQueryWrapper<>(ReportDetailStressDO.class);
        queryWrapper.eq(ReportDetailStressDO::getReportId, reportDTO.getId());
        queryWrapper.orderByDesc(ReportDetailStressDO::getSamplerCount).last("limit 1");

        // 获取第一个快照（注意：可能为 null）
        ReportDetailStressDO oldReportDetailStressDO = reportDetailStressMapper.selectOne(queryWrapper);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            log.error("Time too long,error");
            reportDO.setExecuteState(ReportStateEnum.EXECUTE_FAIL.name());
        }

        ReportDetailStressDO newReportDetailStressDO = reportDetailStressMapper.selectOne(queryWrapper);

        // 【安全检查】如果压测还没开始，newData 可能是 null
        if (newReportDetailStressDO == null) {
            log.warn("尚未获取到明细数据，重新发送 MQ 等待...");
            kafkaTemplate.send(KafkaTopicConfig.REPORT_STATE_TOPIC_NAME, "report_id_" + req.getId(), JsonUtil.obj2Json(req));
            return;
        }

        // 安全获取数量，防止 null 导致方法中断
        long oldCount = (oldReportDetailStressDO == null) ? 0 : oldReportDetailStressDO.getSamplerCount();
        long newCount = newReportDetailStressDO.getSamplerCount();


        if(newCount > oldCount){
            System.err.println("🚀newCount > oldCount " );
            //有新数据，则重新发送MQ消息 说明又有新的压测明细入库了。压测还没彻底结束
            reportDO.setExecuteState(ReportStateEnum.COUNTING_REPORT.name());
            //于是代码重新发送一条 MQ 消息给自己，状态设为 COUNTING_REPORT，触发下一轮循环检查 去消费者
            kafkaTemplate.send(KafkaTopicConfig.REPORT_STATE_TOPIC_NAME,"report_id_"+reportDTO.getId(), JsonUtil.obj2Json(req));
        }else {
            //没更新，则处理完成测试报告 视为执行成功
            reportDO.setExecuteState(ReportStateEnum.EXECUTE_SUCCESS.name());

        }
        // 处理聚合统计信息
// 只有当 endTime 不为空时，才计算时长
        if (reportDTO.getEndTime() != null) {
            reportDO.setEndTime(reportDTO.getEndTime());
            if (reportDO.getStartTime() != null) {
                reportDO.setExpandTime(reportDTO.getEndTime() - reportDO.getStartTime());
            }
        } else {
            // 如果没有结束时间，给个当前时间作为兜底，或者直接设为 0
            reportDO.setEndTime(System.currentTimeMillis());
            reportDO.setExpandTime(reportDO.getEndTime() - reportDO.getStartTime());
        }

        //处理聚合统计信息
        reportDO.setEndTime(reportDTO.getEndTime());
        reportDO.setExpandTime(reportDTO.getEndTime()-reportDO.getStartTime());
        reportDO.setQuantity(newReportDetailStressDO.getSamplerCount());
        reportDO.setFailQuantity(newReportDetailStressDO.getErrorCount());
        reportDO.setPassQuantity(reportDO.getQuantity()-reportDO.getFailQuantity());

        Map<String,Object> summmaryMap = new HashMap<>();
        summmaryMap.put("QPS",newReportDetailStressDO.getRequestRate());
        summmaryMap.put("Error request percentage",newReportDetailStressDO.getErrorPercentage());
        summmaryMap.put("Average response time (milliseconds)",newReportDetailStressDO.getMeanTime());
        summmaryMap.put("Max response time (milliseconds)",newReportDetailStressDO.getMaxTime());
        summmaryMap.put("Min response time (milliseconds)",newReportDetailStressDO.getMinTime());

        reportDO.setSummary(JsonUtil.obj2Json(summmaryMap));

        //更新测试报告
        reportMapper.updateById(reportDO);


    }




}
