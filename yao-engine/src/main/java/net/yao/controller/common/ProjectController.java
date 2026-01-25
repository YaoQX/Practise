package net.yao.controller.common;

import jakarta.servlet.http.HttpServletRequest;
import net.yao.req.common.ProjectDelReq;
import net.yao.req.common.ProjectSaveReq;
import net.yao.req.common.ProjectUpdateReq;
import net.yao.service.common.ProjectService;
import net.yao.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping("/list")
    public JsonData list() {
        return JsonData.buildSuccess(projectService.list());
    }

    @RequestMapping("/save")
    public JsonData save(@RequestBody ProjectSaveReq projectSaveReq) {
        return JsonData.buildSuccess(projectService.save(projectSaveReq));
    }

    @RequestMapping("/update")
    public JsonData update(@RequestBody ProjectUpdateReq projectUpdateReq) {
        return JsonData.buildSuccess(projectService.update(projectUpdateReq));
    }

    @RequestMapping("/delete")
    public JsonData delete(@RequestBody Long id) {
        return JsonData.buildSuccess(projectService.delete(id));
    }

}
