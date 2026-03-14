package net.yao.controller.api;

import jakarta.annotation.Resource;
import net.yao.req.api.ApiCaseModuleDelReq;
import net.yao.req.api.ApiCaseModuleSaveReq;
import net.yao.req.api.ApiCaseModuleUpdateReq;
import net.yao.service.api.ApiCaseModuleService;
import net.yao.util.JsonData;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/api_case_module")
public class ApiCaseModuleController {

    @Resource
    private ApiCaseModuleService apiCaseModuleService;

    /**
     * api列表
     */
    @GetMapping("/list")
    public JsonData list(@RequestParam("projectId") Long projectId) {
        return JsonData.buildSuccess(apiCaseModuleService.list(projectId));
    }

    @GetMapping("/find")
    public JsonData find(@RequestParam("projectId") Long projectId, @RequestParam("moduleId") Long moduleId) {
        return JsonData.buildSuccess(apiCaseModuleService.getById(projectId, moduleId));
    }

    @PostMapping("/save")
    public JsonData save(@RequestBody ApiCaseModuleSaveReq req) {
        return JsonData.buildSuccess(apiCaseModuleService.save(req));
    }


    @PostMapping("/update")
    public JsonData update(@RequestBody ApiCaseModuleUpdateReq req) {
        return JsonData.buildSuccess(apiCaseModuleService.update(req));
    }


    @PostMapping("/delete")
    public JsonData delete(@RequestBody ApiCaseModuleDelReq req) {
        return JsonData.buildSuccess(apiCaseModuleService.del(req));
    }

}
