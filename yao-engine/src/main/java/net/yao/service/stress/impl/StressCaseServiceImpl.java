package net.yao.service.stress.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.yao.config.KafkaTopicConfig;
import net.yao.dto.ReportDTO;
import net.yao.dto.stress.StressCaseDTO;
import net.yao.enums.BizCodeEnum;
import net.yao.enums.ReportStateEnum;
import net.yao.enums.StressSourceTypeEnum;
import net.yao.enums.TestTypeEnum;
import net.yao.exception.BizException;
import net.yao.feign.ReportFeignService;
import net.yao.mapper.EnvironmentMapper;
import net.yao.mapper.StressCaseMapper;
import net.yao.model.EnvironmentDO;
import net.yao.model.StressCaseDO;
import net.yao.req.ReportSaveReq;
import net.yao.req.ReportUpdateReq;
import net.yao.req.stress.StressCaseDelReq;
import net.yao.req.stress.StressCaseSaveReq;
import net.yao.req.stress.StressCaseUpdateReq;
import net.yao.service.stress.StressCaseService;
import net.yao.service.stress.core.BaseStressEngine;
import net.yao.service.stress.core.StressJmxEngine;
import net.yao.service.stress.core.StressSimpleEngine;
import net.yao.util.JsonData;
import net.yao.util.JsonUtil;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class StressCaseServiceImpl implements StressCaseService {

    @Autowired
    private StressCaseMapper stressCaseMapper;

    @Autowired
    private ReportFeignService reportFeignService;

    @Resource
    private EnvironmentMapper environmentMapper;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    // 建议：引入线程池执行压测
    private static final ExecutorService stressExecutor = Executors.newFixedThreadPool(5);

    public StressCaseDTO findById(Long projectId, Long caseId){
        LambdaQueryWrapper<StressCaseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StressCaseDO::getProjectId, projectId)
                .eq(StressCaseDO::getId, caseId);
        StressCaseDO stressCaseDO = stressCaseMapper.selectOne(wrapper);
        return SpringBeanUtil.copyProperties(stressCaseDO, StressCaseDTO.class);

    }

    public int save(StressCaseSaveReq req){
        StressCaseDO stressCaseDO = SpringBeanUtil.copyProperties(req,StressCaseDO.class);
        return stressCaseMapper.insert(stressCaseDO);
    }

    public int delete(Long projectId, Long id){
        LambdaQueryWrapper<StressCaseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StressCaseDO::getProjectId, projectId)
                .eq(StressCaseDO::getId, id);
        return stressCaseMapper.delete(wrapper);
    }

    public int update(StressCaseUpdateReq req) {
        StressCaseDO stressCaseDO = SpringBeanUtil.copyProperties(req, StressCaseDO.class);
        LambdaQueryWrapper<StressCaseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StressCaseDO::getProjectId, stressCaseDO.getProjectId())
                .eq(StressCaseDO::getId, stressCaseDO.getId());
        return stressCaseMapper.update(stressCaseDO, queryWrapper);
    }

    /**
     * 执⾏⽤例
     * 【1】查询⽤例详情
     * 【2】初始化测试报告
     * 【3】判断压测类型 JMX、SIMPLE
     * 【4】初始化测试引擎
     * 【5】组装测试计划
     * 【6】执⾏压测
     * 【7】发送压测结果明细
     * 【8】压测完成清理数数据
     * 【9】通知压测结束
     */
    public int execute(Long projectId, Long caseId) {
        LambdaQueryWrapper <StressCaseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StressCaseDO::getProjectId, projectId)
                .eq(StressCaseDO::getId, caseId);
        StressCaseDO stressCaseDO = stressCaseMapper.selectOne(queryWrapper);
        if(stressCaseDO!=null){
            //初始化测试报告 用例名称 执⾏中 压力测试
            ReportSaveReq reportSaveReq = ReportSaveReq.builder().projectId(stressCaseDO.getProjectId())
                    .caseId(stressCaseDO.getId())
                    .startTime(System.currentTimeMillis())
                    .executeState(ReportStateEnum.EXECUTING.name())
                    .name(stressCaseDO.getName())
                    .type(TestTypeEnum.STRESS.name())
                    .build();
            JsonData jsonData = reportFeignService.save(reportSaveReq);
            if (jsonData.isSuccess()) {
                ReportDTO reportDTO = jsonData.getData(ReportDTO.class);

                //判断压测类型 JMX、SIMPLE 无视大小写
                if (StressSourceTypeEnum.JMX.name().equalsIgnoreCase(stressCaseDO.getStressSourceType())) {


                    // 关键：发送初始状态到 Kafka，通知监控模块（如大屏、实时统计）开始工作
                    sendReportStateUpdate(reportDTO.getId(), ReportStateEnum.EXECUTING);



                    // 使用异步执行，不阻塞当前的 Web 请求

                    new Thread(() -> {
                        try {
                            // 在子线程里：从 Minio 下载 -> 解析 -> 压测
                            runJmxStressCase(stressCaseDO, reportDTO);
                        } catch (Exception e) {
                            log.error("Error ", e);
                            sendReportStateUpdate(reportDTO.getId(), ReportStateEnum.EXECUTE_FAIL);
                        }
                    }, "Stress-Launcher-Thread").start();

                    return 1;


                } else if (StressSourceTypeEnum.SIMPLE.name().equalsIgnoreCase(stressCaseDO.getStressSourceType())) {
                    // 使用异步执行，不阻塞当前的 Web 请求

                    new Thread(() -> {
                        try {
                            // 在子线程里：直接解析DB里结构并组装执行
                            runSimpleStressCase(stressCaseDO, reportDTO);
                        } catch (Exception e) {
                            log.error("Error ", e);
                            sendReportStateUpdate(reportDTO.getId(), ReportStateEnum.EXECUTE_FAIL);

                        }
                    }, "Stress-Launcher-Thread").start();

                    return 1;


                } else {
                    throw new BizException(BizCodeEnum.STRESS_UNSUPPORTED);

                }

            }



        }
        return 0;

    }

    private void runJmxStressCase(StressCaseDO stressCaseDO, ReportDTO reportDTO) {
        //创建引擎
        BaseStressEngine stressEngine = new StressJmxEngine(stressCaseDO,reportDTO,applicationContext);

        //运行压测
        stressEngine.startStressTest();


    }

    private void runSimpleStressCase(StressCaseDO stressCaseDO, ReportDTO reportDTO) {
        EnvironmentDO environmentDO = environmentMapper.selectById(stressCaseDO.getEnvironmentId());
        //创建引擎
        BaseStressEngine stressEngine = new StressSimpleEngine(environmentDO,stressCaseDO,reportDTO,applicationContext);

        //运行压测
        stressEngine.startStressTest();

    }

    /**
     * 封装状态通知逻辑
     */
    private void sendReportStateUpdate(Long reportId, ReportStateEnum state) {
        ReportUpdateReq msg = new ReportUpdateReq();
        msg.setId(reportId);
        msg.setExecuteState(state.name());
        msg.setEndTime(System.currentTimeMillis());
        kafkaTemplate.send(KafkaTopicConfig.REPORT_STATE_TOPIC_NAME, JsonUtil.obj2Json(msg));

    }





}
