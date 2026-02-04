package net.yao.service.common;

import net.yao.dto.common.CaseInfoDTO;
import net.yao.enums.TestTypeEnum;

public interface ResultSenderService {
    /**
     * 发送测试结果
     * @param caseInfoDTO
     * @param reportTypeEnum 种类
     * @param result 测试结果
     */
    void sendResult(CaseInfoDTO caseInfoDTO, TestTypeEnum reportTypeEnum, String result) ;
}
