package net.yao.service.ui;

import net.yao.dto.ui.UiCaseDTO;
import net.yao.dto.ui.UiCaseModuleDTO;
import net.yao.req.ui.UiCaseModuleSaveReq;
import net.yao.req.ui.UiCaseModuleUpdateReq;

import java.util.List;

public interface UiCaseModuleService {

    UiCaseModuleDTO getById(Long projectId, Long caseId);

    int delete(Long projectId, Long caseId);

    List<UiCaseModuleDTO> list(Long projectId);

    int save(UiCaseModuleSaveReq req);

    int update(UiCaseModuleUpdateReq req);
}
