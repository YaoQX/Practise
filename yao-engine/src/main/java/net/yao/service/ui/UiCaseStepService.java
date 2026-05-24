package net.yao.service.ui;

import net.yao.req.ui.UiCaseDelReq;
import net.yao.req.ui.UiCaseStepDelReq;
import net.yao.req.ui.UiCaseStepSaveReq;
import net.yao.req.ui.UiCaseStepUpdateReq;

public interface UiCaseStepService {
    int save(UiCaseStepSaveReq req);

    int update(UiCaseStepUpdateReq req);

    int delete(UiCaseStepDelReq req);
}
