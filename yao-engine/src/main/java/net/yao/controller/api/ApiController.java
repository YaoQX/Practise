package net.yao.controller.api;

import net.yao.req.api.ApiDelReq;
import net.yao.req.api.ApiSaveReq;
import net.yao.req.api.ApiUpdateReq;
import net.yao.service.api.ApiService;
import net.yao.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/api")
public class ApiController {
    @Autowired
    private ApiService apiService;


    /**
     * 根据projectId和id查找
     */
    @GetMapping("/find")
    public JsonData find(@RequestParam("projectId") Long projectId, @RequestParam("id") Long id) {
        return JsonData.buildSuccess(apiService.getById(projectId, id));
    }

    /**
     * 保存接口
     */
    @PostMapping("/save")
    public JsonData save(@RequestBody ApiSaveReq req) {
        return JsonData.buildSuccess(apiService.save(req));
    }

    /**
     * 修改接口
     */
    @PostMapping("/update")
    public JsonData update(@RequestBody ApiUpdateReq req) {
        return JsonData.buildSuccess(apiService.update(req));
    }

    /**
     * 删除接口
     */
    @PostMapping("/delete")
    public JsonData delete(@RequestBody ApiDelReq req) {
        return JsonData.buildSuccess(apiService.delete(req));
    }
}
