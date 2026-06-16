package net.yao.controller;

import net.yao.dto.AccountDTO;
import net.yao.dto.RoleDTO;
import net.yao.req.*;
import net.yao.service.RoleService;
import net.yao.util.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 给角色加权限
     */
    @PostMapping("/api/permit/v1/role/addPermission")
    public JsonData addPermission(@RequestBody RoleAddPermissionReq req) {
        int rows = roleService.addPermission(req);
        return JsonData.buildSuccess(rows);
    }

    /**
     * 给角色删权限
     */
    @PostMapping("/api/permit/v1/role/delPermission")
    public JsonData delPermission(@RequestBody RoleDelPermissionReq req) {
        int rows = roleService.delPermission(req);
        return JsonData.buildSuccess(rows);
    }

    /**
     * 给账号加角色
     */
    @PostMapping("/api/permit/v1/role/addRoleByAccountId")
    public JsonData addRoleByAccountId(@RequestBody AccountRoleAddReq req) {
        int rows = roleService.addRoleByAccountId(req);
        return JsonData.buildSuccess(rows);
    }

    /**
     * 给账号删除角色
     */
    @PostMapping("/api/permit/v1/role/delRoleByAccountId")
    public JsonData delRoleByAccountId(@RequestBody AccountRoleDelReq req) {
        int rows = roleService.delRoleByAccountId(req);
        return JsonData.buildSuccess(rows);
    }

    /**
     * 查看全部角色列表
     */
    @GetMapping("/api/permit/v1/role/list")
    public JsonData list() {
        List<RoleDTO> list = roleService.list();
        return JsonData.buildSuccess(list);
    }

    /**
     * 增加角色
     */
    @PostMapping("/api/permit/v1/role/add")
    public JsonData addRole(@RequestBody RoleAddReq addReq){
        int rows = roleService.addRole(addReq);
        return JsonData.buildSuccess(rows);
    }

    /**
     * 删除角色
     */
    @PostMapping("/api/permit/v1/role/delete")
    public JsonData deleteRole(@RequestBody RoleDelReq delReq){
        int rows = roleService.deleteRole(delReq.getId());
        return JsonData.buildSuccess(rows);
    }

    /**
     * 查找某个账号的角色和权限
     */
    @GetMapping("/api/permit/v1/role/findRoleByAccountId")
    public JsonData findRoleByAccountId(@RequestParam("accountId") Long accountId) {
        AccountDTO accountDTO = roleService.getAccountWithRoleByAccountId(accountId);
        return JsonData.buildSuccess(accountDTO);
    }


}
