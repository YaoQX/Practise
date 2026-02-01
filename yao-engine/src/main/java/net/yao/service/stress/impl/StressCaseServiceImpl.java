package net.yao.service.stress.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.yao.dto.ReportDTO;
import net.yao.dto.stress.StressCaseDTO;
import net.yao.enums.BizCodeEnum;
import net.yao.enums.ReportStateEnum;
import net.yao.enums.StressSourceTypeEnum;
import net.yao.enums.TestTypeEnum;
import net.yao.feign.ReportFeignService;
import net.yao.mapper.StressCaseMapper;
import net.yao.model.EnvironmentDO;
import net.yao.model.StressCaseDO;
import net.yao.req.ReportSaveReq;
import net.yao.req.stress.StressCaseDelReq;
import net.yao.req.stress.StressCaseSaveReq;
import net.yao.req.stress.StressCaseUpdateReq;
import net.yao.service.stress.StressCaseService;
import net.yao.util.JsonData;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StressCaseServiceImpl implements StressCaseService {

    @Autowired
    private StressCaseMapper stressCaseMapper;

    @Autowired
    private ReportFeignService reportFeignService;

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
     * 【4】初始化测试引ක
     * 【5】组装测试计划
     * 【6】执⾏压测
     * 【7】发ᭆ压测结果明细
     * 【8】压测完成清理数ഝ
     * 【9】᭗知压测结束
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
                    //runSimpleStressCase(stressCaseDO, reportDTO);
                } else if (StressSourceTypeEnum.SIMPLE.name().equalsIgnoreCase(stressCaseDO.getStressSourceType())) {
                   // runJmxStressCase(stressCaseDO, reportDTO);

                } else {
                    //throw new BizException(BizCodeEnum.STRESS_UNSUPPORTED);

                }
                JsonData.buildSuccess();
            }


        }
        return 0;

    }





}
