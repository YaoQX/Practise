package net.yao.controller;

import net.yao.dto.PermissionDTO;
import net.yao.service.PermissionService;
import net.yao.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取全部权限
     */
    @GetMapping("/api/permit/v1/permission/list")
    public JsonData getAllPermission() {
        List<PermissionDTO> list = permissionService.list();
        return JsonData.buildSuccess(list);
    }


}
