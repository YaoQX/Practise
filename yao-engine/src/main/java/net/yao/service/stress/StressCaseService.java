package net.yao.service.stress;

import net.yao.dto.stress.StressCaseDTO;
import net.yao.req.stress.StressCaseDelReq;
import net.yao.req.stress.StressCaseSaveReq;
import net.yao.req.stress.StressCaseUpdateReq;

public interface StressCaseService {
    StressCaseDTO findById(Long projectId, Long caseId);

    int delete(Long projectId, Long id);

    int save(StressCaseSaveReq req);

    int update(StressCaseUpdateReq req);

    int execute(Long projectId, Long caseId);
}
