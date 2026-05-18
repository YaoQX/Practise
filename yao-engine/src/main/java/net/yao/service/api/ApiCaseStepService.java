package net.yao.service.api;

import net.yao.req.api.ApiCaseStepSaveReq;
import net.yao.req.api.ApiCaseStepUpdateReq;

public interface ApiCaseStepService {

    int save(ApiCaseStepSaveReq req);

    int update(ApiCaseStepUpdateReq req);

    int del(Long projectId,Long id);
}
