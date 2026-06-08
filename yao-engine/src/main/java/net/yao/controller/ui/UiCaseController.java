package net.yao.controller.ui;

import io.swagger.v3.core.util.Json;
import jakarta.annotation.Resource;
import net.yao.dto.ui.UiCaseDTO;
import net.yao.req.ui.UiCaseDelReq;
import net.yao.req.ui.UiCaseSaveReq;
import net.yao.req.ui.UiCaseUpdateReq;
import net.yao.service.ui.UiCaseService;
import net.yao.util.JsonData;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/ui_case")
public class UiCaseController {

    @Resource
    private UiCaseService uiCaseService;

    @RequestMapping("/update")
    public JsonData update(@RequestBody UiCaseUpdateReq req)
    {
        return JsonData.buildSuccess(uiCaseService.update(req));
    }

    @RequestMapping("/del")
    public JsonData delete(@RequestBody UiCaseDelReq req)
    {
        return JsonData.buildSuccess(uiCaseService.delete(req));
    }


    @RequestMapping("/find")
    public JsonData find(@RequestParam("projectId") Long projectId, @RequestParam("id") Long caseId)
    {
        UiCaseDTO uiCaseDTO = uiCaseService.find(projectId, caseId);
        return JsonData.buildSuccess(uiCaseDTO);
    }

    /**
     * 新增
     */
    @RequestMapping("/save")
    public JsonData save(@RequestBody UiCaseSaveReq req)
    {
        return JsonData.buildSuccess(uiCaseService.save(req));
    }

    @GetMapping("/execute")
    public JsonData execute(@RequestParam("projectId") Long projectId, @RequestParam("id") Long caseId){

        JsonData jsonData = uiCaseService.execute(projectId, caseId);

        return  jsonData;
    }

}
