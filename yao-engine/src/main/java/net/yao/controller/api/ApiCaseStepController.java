package net.yao.controller.api;

import net.yao.req.api.ApiCaseStepDelReq;
import net.yao.req.api.ApiCaseStepSaveReq;
import net.yao.req.api.ApiCaseStepUpdateReq;
import net.yao.service.api.ApiCaseStepService;
import net.yao.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/api_case_step")
public class ApiCaseStepController {

    @Autowired
    private ApiCaseStepService apiCaseStepService;

    @PostMapping("/save")
    public JsonData save(@RequestBody ApiCaseStepSaveReq req) {
        return JsonData.buildSuccess(apiCaseStepService.save(req));
    }

    @PostMapping("/update")
    public JsonData update(@RequestBody ApiCaseStepUpdateReq req) {
        return JsonData.buildSuccess(apiCaseStepService.update(req));
    }

    @PostMapping("/delete")
    public JsonData delete(@RequestBody ApiCaseStepDelReq req) {
        return JsonData.buildSuccess(apiCaseStepService.del(req.getProjectId(), req.getId()));
    }
}
