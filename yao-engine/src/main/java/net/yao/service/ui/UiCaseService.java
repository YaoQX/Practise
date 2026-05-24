package net.yao.service.ui;

import net.yao.dto.ui.UiCaseDTO;
import net.yao.req.ui.UiCaseDelReq;
import net.yao.req.ui.UiCaseSaveReq;
import net.yao.req.ui.UiCaseUpdateReq;
import net.yao.util.JsonData;

public interface UiCaseService {

    UiCaseDTO find(Long projectId, Long caseId);

    int Delete(UiCaseDelReq req);

    int update(UiCaseUpdateReq req);

    int save(UiCaseSaveReq req);

    JsonData execute(Long projectId, Long caseId);

}
