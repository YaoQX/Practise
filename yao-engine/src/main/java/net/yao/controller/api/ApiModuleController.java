package net.yao.controller.api;

import net.yao.req.api.ApiModuleDelReq;
import net.yao.req.api.ApiModuleSaveReq;
import net.yao.service.api.ApiModuleService;
import net.yao.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/api_module")
public class ApiModuleController {

    @Autowired
    private ApiModuleService apiModuleService;

    @GetMapping("/list")
    public JsonData list(@RequestParam("projectId") Long projectId) {
        return JsonData.buildSuccess(apiModuleService.list(projectId));
    }

    /**
     * 根据id查找
     */
    @GetMapping("/find")
    public JsonData find(@RequestParam("projectId") Long projectId, @RequestParam("moduleId") Long moduleId) {
        return JsonData.buildSuccess(apiModuleService.getById(projectId,moduleId));
    }

    /**
     * 根据Id删除
     */
    @GetMapping("/delete")
    public JsonData delete(ApiModuleDelReq req) {
        return JsonData.buildSuccess(apiModuleService.delete(req.getId(),req.getProjectId()));
    }

    @GetMapping("/save")
    public JsonData save(ApiModuleSaveReq req) {
        return JsonData.buildSuccess(apiModuleService.save(req));
    }

}
