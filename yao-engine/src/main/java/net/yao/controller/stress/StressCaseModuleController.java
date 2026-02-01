package net.yao.controller.stress;

import net.yao.req.stress.*;
import net.yao.service.stress.StressCaseModuleService;
import net.yao.service.stress.StressCaseService;
import net.yao.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stress_case_module")
public class StressCaseModuleController {

    @Autowired
    private StressCaseModuleService stressCaseModuleService;

    @GetMapping("/list")
    public JsonData list(@RequestParam("projectId")Long projectId){
        return JsonData.buildSuccess(stressCaseModuleService.list(projectId));
    }

    @GetMapping("/find")
    public JsonData findById(@RequestParam("projectId") Long projectId, @RequestParam("id") Long moduleId){
        return JsonData.buildSuccess(stressCaseModuleService.findById(projectId,moduleId));
    }


    @PostMapping("/del")
    public JsonData delete(@RequestBody StressCaseModuleDelReq req){
        return JsonData.buildSuccess(stressCaseModuleService.delete(req.getProjectId(),req.getId()));
    }
    @PostMapping("/save")
    public JsonData save(@RequestBody StressCaseModuleSaveReq req){
        return JsonData.buildSuccess(stressCaseModuleService.save(req));
    }

    @PostMapping("/update")
    public JsonData update(@RequestBody StressCaseModuleUpdateReq req){
        return JsonData.buildSuccess(stressCaseModuleService.update(req));
    }
}
