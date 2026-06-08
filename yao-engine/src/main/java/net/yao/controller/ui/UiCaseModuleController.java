package net.yao.controller.ui;

import jakarta.annotation.Resource;
import net.yao.dto.ui.UiCaseModuleDTO;
import net.yao.req.ui.UiCaseModuleDelReq;
import net.yao.req.ui.UiCaseModuleSaveReq;
import net.yao.req.ui.UiCaseModuleUpdateReq;
import net.yao.service.ui.UiCaseModuleService;
import net.yao.util.JsonData;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ui_case_module")
public class UiCaseModuleController {


    @Resource
    private UiCaseModuleService caseModuleService;

    /**
     * 获取项目模块列表
     */
    @RequestMapping("/list")
    public JsonData list(@RequestParam("projectId") Long projectId)
    {
        List<UiCaseModuleDTO> list = caseModuleService.list(projectId);
        return JsonData.buildSuccess(list);
    }

    /**
     * 获取项目模块详情
     * @param projectId 项目ID
     * @param moduleId 模块ID
     */
    @RequestMapping("/find")
    public JsonData find(@RequestParam("projectId") Long projectId, @RequestParam("moduleId") Long moduleId)
    {
        UiCaseModuleDTO uiCaseModuleDTO =  caseModuleService.getById(projectId,moduleId);
        return JsonData.buildSuccess(uiCaseModuleDTO);
    }

    /**
     * 保存项目模块
     */
    @RequestMapping("/save")
    public JsonData save(@RequestBody UiCaseModuleSaveReq req)
    {
        return JsonData.buildSuccess(caseModuleService.save(req));
    }

    /**
     * 更新项目模块
     */
    @RequestMapping("/update")
    public JsonData update(@RequestBody UiCaseModuleUpdateReq req){
        return JsonData.buildSuccess(caseModuleService.update(req));
    }

    /**
     * 删除项目模块
     */
    @RequestMapping("/del")
    public JsonData delete(@RequestBody UiCaseModuleDelReq req)
    {
        return JsonData.buildSuccess(caseModuleService.delete(req.getProjectId(),req.getId()));
    }

}



