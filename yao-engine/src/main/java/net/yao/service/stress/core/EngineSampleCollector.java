package net.yao.service.stress.core;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.yao.dto.ReportDTO;
import net.yao.dto.StressSampleResultDTO;
import net.yao.dto.common.CaseInfoDTO;
import net.yao.enums.TestTypeEnum;
import net.yao.model.StressCaseDO;
import net.yao.service.common.ResultSenderService;
import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.reporters.Summariser;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.SamplingStatCalculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义结果收集器
 * 作用：拦截 JMeter 的每一次请求结果
 */
@Slf4j
public class EngineSampleCollector extends ResultCollector {

    /**
     * Key (String): 通常是接口名称（Label，例如 "登录接口", "查询订单"）。
     *
     * Value (SamplingStatCalculator): 这是 JMeter 提供的一个非常好用的类（位于 org.apache.jmeter.visualizers 包下）。它能自动帮你计算 Avg, Max, Min, TP99, Error%, QPS 等指标。
     */
    private Map<String, SamplingStatCalculator> calculatorMap = new ConcurrentHashMap<>();
    private ResultSenderService resultSenderService;
    private ReportDTO reportDTO;
    private StressCaseDO stressCaseDO;

    public EngineSampleCollector() {
        super();
    }

    /**
     *
     * @param stressCaseDO  用例
     * @param summariser 用于看日志。在后台日志里留个底，证明程序还在跑
     * @param resultSenderService 发送对象
     * @param reportDTO 测试报告
     */
    public EngineSampleCollector(StressCaseDO stressCaseDO, Summariser summariser, ResultSenderService resultSenderService, ReportDTO reportDTO) {
        super(summariser);
        this.stressCaseDO = stressCaseDO;
        this.resultSenderService = resultSenderService;
        this.reportDTO = reportDTO;
    }


    /**
     * 核心方法：当发生一次采样（请求）时，JMeter 会自动调用这个方法
     * @param event 采样事件，包含结果数据
     */

    public void sampleOccurred(SampleEvent event) {
        // 1. 必须保留父类逻辑 (父类可能要负责把结果写文件等标准操作)
        super.sampleOccurred(event);

        // 2. 获取本次请求的结果对象
        SampleResult result = event.getResult();

        String sampleLabel = result.getSampleLabel();

        SamplingStatCalculator calculator = calculatorMap.get(sampleLabel);
        if (calculator == null) {
            calculator = new SamplingStatCalculator();
            calculator.addSample(result);
            calculatorMap.put(sampleLabel, calculator);

        }
        else{
            //如果计算器存在，就添加更新采样器结果
            calculator.addSample(result);
        }
        //封装采样器结果数据
        StressSampleResultDTO sampleResultInfoDTO = new StressSampleResultDTO();
        //测试报告id
        sampleResultInfoDTO.setReportId(reportDTO.getId());
        // 设置时间戳
        sampleResultInfoDTO.setSampleTime(result.getTimeStamp());
        // 设置请求标签
        sampleResultInfoDTO.setSamplerLabel(result.getSampleLabel());
        // 设置样本计数
        sampleResultInfoDTO.setSamplerCount(calculator.getCount());
        // 设置平均时间
        sampleResultInfoDTO.setMeanTime(calculator.getMean());
        // 设置最小时间
        sampleResultInfoDTO.setMinTime(calculator.getMin().intValue());
        // 设置最大时间
        sampleResultInfoDTO.setMaxTime(calculator.getMax().intValue());

        // 设置错误百分比
        sampleResultInfoDTO.setErrorPercentage(calculator.getErrorPercentage());
        // 设置错误计数
        sampleResultInfoDTO.setErrorCount(calculator.getErrorCount());
        // 设置请求速率
        sampleResultInfoDTO.setRequestRate(calculator.getRate());
        // 设置接收数据大小
        sampleResultInfoDTO.setReceiveKBPerSecond(calculator.getKBPerSecond());
        // 设置发送数据大小
        sampleResultInfoDTO.setSentKBPerSecond(calculator.getSentKBPerSecond());


        //设置请求路径参数
        sampleResultInfoDTO.setRequestLocation(event.getResult().getUrlAsString());
        // 设置请求头
        sampleResultInfoDTO.setRequestHeader(event.getResult().getRequestHeaders());
        // 设置请求体
        sampleResultInfoDTO.setRequestBody(event.getResult().getSamplerData());
        // 设置响应码
        sampleResultInfoDTO.setResponseCode(event.getResult().getResponseCode());
        // 设置响应头
        sampleResultInfoDTO.setResponseHeader(event.getResult().getResponseHeaders());
        // 设置响应数据
        sampleResultInfoDTO.setResponseData(event.getResult().getResponseDataAsString());

         //断言 拿到这些检查的结果
        AssertionResult[] assertionResults = event.getResult().getAssertionResults();
        // StringBuilder 是“可变” 省资源
        StringBuilder assertMsg = new StringBuilder();
        if (Objects.nonNull(assertionResults)) {
            for (AssertionResult assertionResult : assertionResults) {
                assertMsg.append("name=").append(assertionResult.getName())
                        .append(",msg=").append(assertionResult.getFailureMessage()).append(",");
            }
        }
        //错误信息
        sampleResultInfoDTO.setAssertInfo(assertMsg.toString());
        //序列化为json对象
        String sampleResultInfoJson = JSON.toJSONString(sampleResultInfoDTO);
        log.error(sampleResultInfoJson);
        //发送测试结果
        CaseInfoDTO caseInfoDTO = new CaseInfoDTO(stressCaseDO.getId(),stressCaseDO.getModuleId(),stressCaseDO.getName());
        resultSenderService.sendResult(caseInfoDTO, TestTypeEnum.STRESS,sampleResultInfoJson);



    }


}
