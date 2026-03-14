package net.yao.service.api;



import net.yao.dto.api.ApiCaseModuleDTO;
import net.yao.req.api.ApiCaseModuleDelReq;
import net.yao.req.api.ApiCaseModuleSaveReq;
import net.yao.req.api.ApiCaseModuleUpdateReq;

import java.util.List;

public interface ApiCaseModuleService {

    List<ApiCaseModuleDTO> list(Long projectId);

    ApiCaseModuleDTO getById(Long projectId, Long moduleId);

    int save(ApiCaseModuleSaveReq req);

    int update(ApiCaseModuleUpdateReq req);

    int del(ApiCaseModuleDelReq req);
}
