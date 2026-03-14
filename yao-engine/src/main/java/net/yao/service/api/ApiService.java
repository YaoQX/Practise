package net.yao.service.api;

import net.yao.dto.api.ApiDTO;
import net.yao.req.api.ApiDelReq;
import net.yao.req.api.ApiSaveReq;
import net.yao.req.api.ApiUpdateReq;

public interface ApiService {
    ApiDTO getById(Long projectId, Long id);

    int save(ApiSaveReq req);

    int update(ApiUpdateReq req);

    int delete(ApiDelReq req);
}
