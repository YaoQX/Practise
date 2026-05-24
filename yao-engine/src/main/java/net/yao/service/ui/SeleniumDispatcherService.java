package net.yao.service.ui;

import net.yao.dto.UiOperationResultDTO;
import net.yao.model.UiCaseStepDO;

public interface SeleniumDispatcherService {

    UiOperationResultDTO operationDispatcher(UiCaseStepDO uiCaseStepDO);
}
