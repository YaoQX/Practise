package net.yao.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.yao.dto.*;
import net.yao.enums.ReportStateEnum;
import net.yao.mapper.ReportDetailApiMapper;
import net.yao.mapper.ReportDetailStressMapper;
import net.yao.mapper.ReportDetailUiMapper;
import net.yao.mapper.ReportMapper;
import net.yao.model.ReportDO;
import net.yao.model.ReportDetailApiDO;
import net.yao.model.ReportDetailStressDO;
import net.yao.model.ReportDetailUiDO;
import net.yao.service.ReportDetailService;
import net.yao.util.JsonUtil;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ReportDetailServiceImpl implements ReportDetailService {

    @Autowired
    private ReportDetailStressMapper reportDetailStressMapper;

    @Autowired
    private ReportDetailApiMapper reportDetailApiMapper;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private ReportDetailUiMapper reportDetailUiMapper;

    public void handleStressReportDetail(String topicContent){
        //通过 JsonUtil 将其“反序列化”，从一串枯燥的文本转成了一个有灵魂的 Java 对象 StressSampleResultDTO。
        StressSampleResultDTO  stressSampleResultDTO = JsonUtil.json2Obj(topicContent, StressSampleResultDTO.class);
        //DO (Data Object): 专门映射数据库表的实体类。
        ReportDetailStressDO reportDetailStressDO = SpringBeanUtil.copyProperties(stressSampleResultDTO, ReportDetailStressDO.class);

        reportDetailStressMapper.insert(reportDetailStressDO);
    }

    public void handleApiReportDetail(String topicContent){

        ApiCaseResultDTO apiCaseResultDTO = JsonUtil.json2Obj(topicContent, ApiCaseResultDTO.class);

        ReportDO reportDO = reportMapper.selectById(apiCaseResultDTO.getReportId());

        // 先处理概述
        reportDO.setExecuteState(ReportStateEnum.EXECUTE_SUCCESS.name());
        reportDO.setEndTime(apiCaseResultDTO.getEndTime());
        reportDO.setExpandTime(apiCaseResultDTO.getExpendTime());
        reportDO.setExpandTime(apiCaseResultDTO.getStartTime());
        reportDO.setQuantity(Long.valueOf(apiCaseResultDTO.getQuantity()));
        reportDO.setPassQuantity(Long.valueOf(apiCaseResultDTO.getPassQuantity()));
        reportDO.setFailQuantity(Long.valueOf(apiCaseResultDTO.getFailQuantity()));
        reportMapper.updateById(reportDO);

        //处理测试报告明细
        List<ApiCaseResultItemDTO> apiStepList = apiCaseResultDTO.getList();

        apiStepList.forEach(item->{

            ReportDetailApiDO reportDetailApiDO = SpringBeanUtil.copyProperties(item, ReportDetailApiDO.class);
            ApiCaseStepDTO step = item.getApiCaseStep();
            reportDetailApiDO.setEnvironmentId(step.getEnvironmentId());
            reportDetailApiDO.setCaseId(step.getCaseId());
            reportDetailApiDO.setNum(step.getNum());
            reportDetailApiDO.setName(step.getName());
            reportDetailApiDO.setDescription(step.getDescription());
            reportDetailApiDO.setAssertion(step.getAssertion());
            reportDetailApiDO.setRelation(step.getRelation());
            reportDetailApiDO.setPath(step.getPath());
            reportDetailApiDO.setMethod(step.getMethod());
            reportDetailApiDO.setQuery(step.getQuery());
            reportDetailApiDO.setHeader(step.getHeader());
            reportDetailApiDO.setBody(step.getBody());
            reportDetailApiDO.setBodyType(step.getBodyType());
            reportDetailApiMapper.insert(reportDetailApiDO);
        });


    }

    public void handleUiReportDetail(String topicContent){
        UiCaseResultDTO uiCaseResultDTO = JsonUtil.json2Obj(topicContent, UiCaseResultDTO.class);
        ReportDO reportDO = reportMapper.selectById(uiCaseResultDTO.getReportId());
        reportDO.setExecuteState(ReportStateEnum.EXECUTE_SUCCESS.name());
        reportDO.setEndTime(uiCaseResultDTO.getEndTime());
        reportDO.setExpandTime(uiCaseResultDTO.getExpendTime());
        reportDO.setQuantity(Long.valueOf(uiCaseResultDTO.getQuantity()));
        reportDO.setPassQuantity(Long.valueOf(uiCaseResultDTO.getPassQuantity()));
        reportDO.setFailQuantity(Long.valueOf(uiCaseResultDTO.getFailQuantity()));

        reportMapper.updateById(reportDO);

        List<UiCaseResultItemDTO> uiStepList = uiCaseResultDTO.getList();

        //处理报告明细
        uiStepList.forEach(item->{
            ReportDetailUiDO reportDetailUiDO = SpringBeanUtil.copyProperties(item, ReportDetailUiDO.class);
            UiCaseStepDTO uiCaseStep = item.getUiCaseStep();
            SpringBeanUtil.copyProperties(uiCaseStep, reportDetailUiDO);
            reportDetailUiDO.setId(null);
            reportDetailUiMapper.insert(reportDetailUiDO);
        });

    }


}
