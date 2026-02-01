package net.yao.service.stress;

import net.yao.dto.stress.StressCaseDTO;
import net.yao.dto.stress.StressCaseModuleDTO;
import net.yao.req.stress.StressCaseSaveReq;
import net.yao.req.stress.StressCaseUpdateReq;

public interface StressCaseModuleService {
    StressCaseModuleDTO findById(Long projectId, Long moduleId);

    int delete(Long projectId, Long id);

    int save(StressCaseSaveReq req);

    int update(StressCaseUpdateReq req);
}
