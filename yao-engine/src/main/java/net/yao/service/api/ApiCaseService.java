package net.yao.service.api;

import net.yao.dto.api.ApiCaseDTO;
import net.yao.req.api.ApiCaseSaveReq;
import net.yao.req.api.ApiCaseUpdateReq;
import net.yao.util.JsonData;

public interface ApiCaseService {
    ApiCaseDTO getById(Long projectId, Long id);

    int save(ApiCaseSaveReq req);

    int update(ApiCaseUpdateReq req);

    int del(Long projectId,Long id);

    JsonData execute(Long projectId, Long caseId);
}
