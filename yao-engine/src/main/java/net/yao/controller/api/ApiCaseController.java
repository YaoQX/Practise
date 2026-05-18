package net.yao.controller.api;


import net.yao.req.api.ApiCaseDelReq;
import net.yao.req.api.ApiCaseSaveReq;

import net.yao.req.api.ApiCaseUpdateReq;
import net.yao.service.api.ApiCaseService;

import net.yao.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/api_case")
public class ApiCaseController {

    @Autowired
    private ApiCaseService apiCaseService;

    /**
     * 根据id查找
     */
    @GetMapping("/find")
    public JsonData find(@RequestParam("projectId") Long projectId, @RequestParam("id") Long id) {
        return JsonData.buildSuccess(apiCaseService.getById(projectId,id));
    }

    /**
     * 根据Id删除
     */
    @PostMapping("/delete")
    public JsonData delete(@RequestBody ApiCaseDelReq req) {
        return JsonData.buildSuccess(apiCaseService.del(req.getId(),req.getProjectId()));
    }

    @PostMapping("/save")
    public JsonData save(@RequestBody ApiCaseSaveReq req) {
        return JsonData.buildSuccess(apiCaseService.save(req));
    }

    @PostMapping("/update")
    public JsonData update(@RequestBody ApiCaseUpdateReq req) {
        return JsonData.buildSuccess(apiCaseService.update(req));
    }

    @GetMapping("execute")
    public JsonData execute(@RequestParam("projectId") Long projectId, @RequestParam("id") Long caseId){
        return apiCaseService.execute(projectId,caseId);
    }
}
