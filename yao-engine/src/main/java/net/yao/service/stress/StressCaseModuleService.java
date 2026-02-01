package net.yao.service.stress;

import net.yao.dto.stress.StressCaseDTO;
import net.yao.dto.stress.StressCaseModuleDTO;
import net.yao.req.stress.StressCaseModuleSaveReq;
import net.yao.req.stress.StressCaseModuleUpdateReq;
import net.yao.req.stress.StressCaseSaveReq;
import net.yao.req.stress.StressCaseUpdateReq;

import java.util.List;

public interface StressCaseModuleService {
    StressCaseModuleDTO findById(Long projectId, Long moduleId);

    int delete(Long projectId, Long id);

    int save(StressCaseModuleSaveReq req);

    int update(StressCaseModuleUpdateReq req);

    List<StressCaseModuleDTO> list(Long projectId);
}
