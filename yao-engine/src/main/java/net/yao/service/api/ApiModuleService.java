package net.yao.service.api;

import net.yao.dto.api.ApiModuleDTO;
import net.yao.req.api.ApiModuleSaveReq;
import net.yao.req.api.ApiModuleUpdateReq;

import java.util.List;

public interface ApiModuleService {
    List<ApiModuleDTO> list(Long projectId);

    ApiModuleDTO getById(Long projectId, Long moduleId);

    int delete(Long id, Long projectId);

    int save(ApiModuleSaveReq req);

    int update(ApiModuleUpdateReq req);
}
