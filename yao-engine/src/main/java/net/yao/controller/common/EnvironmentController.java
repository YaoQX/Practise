package net.yao.controller.common;

import net.yao.req.common.EnvironmentDelReq;
import net.yao.req.common.EnvironmentSaveReq;
import net.yao.req.common.EnvironmentUpdateReq;
import net.yao.service.common.EnvironmentService;
import net.yao.util.JsonData;
import org.apache.ibatis.mapping.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/env")
public class EnvironmentController {

    @Autowired
    private EnvironmentService environmentService;

    @RequestMapping("/list")
    public JsonData list(@RequestParam("projectId") Long projectId){
        return JsonData.buildSuccess(environmentService.list(projectId));
    }

    @PostMapping("/save")
    public JsonData save(@RequestBody EnvironmentSaveReq req){
        return JsonData.buildSuccess(environmentService.save(req));
    }

    @PostMapping("/update")
    public JsonData update(@RequestBody EnvironmentUpdateReq req){
        return JsonData.buildSuccess(environmentService.update(req));
    }

    @PostMapping("/delete")
    public JsonData delete(@RequestBody EnvironmentDelReq req){
        return JsonData.buildSuccess(environmentService.delete(req.getProjectId(),req.getId()));
    }

}
