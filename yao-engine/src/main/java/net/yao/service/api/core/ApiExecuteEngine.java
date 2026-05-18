package net.yao.service.api.core;

import io.micrometer.common.util.StringUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.yao.dto.ApiCaseResultDTO;
import net.yao.dto.ApiCaseResultItemDTO;
import net.yao.dto.ApiCaseStepDTO;
import net.yao.dto.ReportDTO;
import net.yao.dto.common.CaseInfoDTO;
import net.yao.enums.ApiBodyTypeEnum;
import net.yao.enums.TestTypeEnum;
import net.yao.exception.BizException;
import net.yao.mapper.EnvironmentMapper;
import net.yao.model.ApiCaseStepDO;
import net.yao.model.EnvironmentDO;
import net.yao.service.common.ResultSenderService;
import net.yao.util.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.hutool.core.collection.ListUtil.toList;


public class ApiExecuteEngine {

    private ReportDTO reportDTO;

    private EnvironmentMapper environmentMapper;

    private ResultSenderService resultSenderService;

    public ApiExecuteEngine(ReportDTO reportDTO){
        this.reportDTO = reportDTO;
        this.environmentMapper = SpringContextHolder.getBean(EnvironmentMapper.class);
        this.resultSenderService = (ResultSenderService) SpringContextHolder.getBean("kafkaSenderServiceImpl");
    }

    /**
     * 一个用例包含多个接口步骤时，如何逐步执行、如何把上一步结果传给下一步、如何最终汇总结果。
     * 逐个执行接口步骤
     *   ↓
     * 处理关联参数 relation
     *   ↓
     * 处理断言 assertion
     *   ↓
     * 生成 ApiCaseResultDTO
     *   ↓
     * resultSenderService.sendResult()
     *   ↓
     * data 服务消费结果，保存报告
     * @param caseInfoDTO
     * @param apiCaseStepDOList
     * @return
     */
    public ApiCaseResultDTO execute(CaseInfoDTO caseInfoDTO, List<ApiCaseStepDO> apiCaseStepDOList){

        try{
            int quantity = apiCaseStepDOList.size();
            long startTime = System.currentTimeMillis();

            ApiCaseResultDTO result = doExecute(null, apiCaseStepDOList);
            long endTime = System.currentTimeMillis();
            result.setStartTime(startTime);
            result.setReportId(reportDTO.getId());
            result.setEndTime(endTime);
            result.setExpendTime(endTime-startTime);
            result.setQuantity(quantity);

            int passQuantity = result.getList().stream().filter(item->{
                    item.setReportId(reportDTO.getId());

                    return item.getExecuteState() && item.getAssertionState();
        }).toList().size();

            result.setPassQuantity(passQuantity);
            result.setFailQuantity(quantity-passQuantity);
            result.setExecuteState(Objects.equals(result.getQuantity(), result.getPassQuantity()));

            //发送结果
            resultSenderService.sendResult(caseInfoDTO, TestTypeEnum.API, JsonUtil.obj2Json(result));
            return result;
    }finally {
            //释放相关资源
            ApiRelationContext.remove();
        }

    }

    /**
     * 递归执行 API 用例步骤列表 stepList，
     * 每执行一个步骤，就把这个步骤的请求、响应、断言结果、异常信息保存到 result 里，
     * 最后返回完整的 ApiCaseResultDTO。
     */
    private ApiCaseResultDTO doExecute(ApiCaseResultDTO result, List<ApiCaseStepDO> stepList) {

        if(result == null){
            result = new ApiCaseResultDTO();
            result.setList(new ArrayList<>(stepList.size()));
        }
        //如果没有 API 步骤需要执行了，就结束方法，返回当前结果。
        if(stepList == null || stepList.isEmpty())
        {
            return result;
        }

        ApiCaseStepDO step = stepList.get(0);
        ApiCaseResultItemDTO resultItem = new ApiCaseResultItemDTO();
        resultItem.setApiCaseStep(SpringBeanUtil.copyProperties(step, ApiCaseStepDTO.class));
        resultItem.setExecuteState(true);
        resultItem.setAssertionState(true);
        result.getList().add(resultItem);

        EnvironmentDO environmentDO = environmentMapper.selectById(step.getEnvironmentId());
        String base = getBaseUrl(environmentDO);

        ApiRequest request = new ApiRequest(base, step.getPath(), step.getAssertion(), step.getRelation(), step.getQuery(), step.getHeader(), step.getBody(), step.getBodyType());
        //生成一个 RestAssured 可以执行的请求对象。
        RequestSpecification given = request.createRequest();
        try{
            long startTime = System.currentTimeMillis();
            //发起请求
            Response response = given.request(step.getMethod())
                    .thenReturn();
            long endTime = System.currentTimeMillis();

            resultItem.setExpendTime(endTime - startTime);
            resultItem.setRequestHeader(JsonUtil.obj2Json(request.getHeaderList()));
            resultItem.setRequestQuery(JsonUtil.obj2Json(request.getQueryList()));
            if(StringUtils.isNotBlank(request.getRequestBody().getBody())){
                if(step.getBodyType().equals(ApiBodyTypeEnum.JSON.name())){
                    resultItem.setRequestBody(request.getRequestBody().getBody());
                }else {
                    resultItem.setRequestBody(JsonUtil.obj2Json(request.getBodyList()));
                }
            }
            //处理响应结果
            resultItem.setResponseBody(response.getBody().asString());
            resultItem.setResponseHeader(JsonUtil.obj2Json(response.getHeaders()));

            //关联取值
            ApiRelationSaveUtil.dispatcher(request,response);

            //断言处理
            ApiAssertionUtil.dispatcher(request,response);


        }catch (BizException e){
            e.printStackTrace();
            //断言失败
            resultItem.setAssertionState(false);
            resultItem.setExceptionMsg(e.getDetail());
        }catch (Exception e){
            e.printStackTrace();
            resultItem.setExecuteState(false);
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            resultItem.setExceptionMsg(sw.toString());
        }

        //下轮递归
        stepList.remove(0);
        return doExecute(result,stepList);




    }

    private static String getBaseUrl(EnvironmentDO environmentDO){
        return environmentDO.getProtocol() + "://" + environmentDO.getDomain() + ":" + environmentDO.getPort();
    }



}
