package net.yao.service.common.impl;

import net.yao.dto.common.CaseInfoDTO;
import net.yao.enums.TestTypeEnum;
import net.yao.service.common.ResultSenderService;
import org.springframework.stereotype.Service;


@Service
public class RedisResultSenderServiceImpl implements ResultSenderService {
    @Override
    public void sendResult(CaseInfoDTO caseInfoDTO, TestTypeEnum testTypeEnum, String result) {

    }
}
