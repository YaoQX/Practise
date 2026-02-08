package net.yao.service.common.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.yao.config.KafkaTopicConfig;
import net.yao.dto.common.CaseInfoDTO;
import net.yao.enums.TestTypeEnum;
import net.yao.service.common.ResultSenderService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class KafkaSenderServiceImpl implements ResultSenderService {

    private static final String TOPIC_KEY = "case_id_";

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    /**
     * 发送测试结果
     * @param caseInfoDTO
     * @param testTypeEnum 种类
     * @param result 测试结果
     */
    @Override
    public void sendResult(CaseInfoDTO caseInfoDTO, TestTypeEnum testTypeEnum, String result) {

        switch (testTypeEnum) {
            case STRESS:
                sendStressResult(caseInfoDTO, result);
                break;
            case API:
                sendPerformanceResult(caseInfoDTO, result);
                break;
            case UI:
                sendUiResult(caseInfoDTO, result);
                break;
            default:
                log.error("Unknown Type");
                break;
        }


    }

    /**
     * 发送压测结果明细
     * @param caseInfoDTO
     * @param result
     * @send topic key message
     */
    public void sendStressResult(CaseInfoDTO caseInfoDTO, String result) {
        kafkaTemplate.send(KafkaTopicConfig.STRESS_TOPIC_NAME, TOPIC_KEY + caseInfoDTO.getId(), result);

    }

    /**
     * 发送接口测试结果明细
     * @param caseInfoDTO
     * @param result
     * @send topic key message
     */
    public void sendPerformanceResult(CaseInfoDTO caseInfoDTO, String result) {
        kafkaTemplate.send(KafkaTopicConfig.API_TOPIC_NAME, TOPIC_KEY + caseInfoDTO.getId(), result);
    }

    /**
     * 发送UI测试结果明细
     * @param caseInfoDTO
     * @param result
     * @send topic key message
     */
    public void sendUiResult(CaseInfoDTO caseInfoDTO, String result) {
        kafkaTemplate.send(KafkaTopicConfig.UI_TOPIC_NAME, TOPIC_KEY + caseInfoDTO.getId(), result);
    }

}
