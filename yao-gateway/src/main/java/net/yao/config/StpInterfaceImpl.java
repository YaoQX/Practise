package net.yao.config;

import cn.dev33.satoken.stp.StpInterface;
import net.yao.service.PermissionService;
import net.yao.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 1. 核心方法：返回一个账号所拥有的权限集合
     */
    public List<String> getPermissionList(Object loginId, String loginType) {

        if(loginId == null){
            return new ArrayList<>();
        }

        Long accountId = Long.parseLong(loginId.toString());
        List<String> permissions = permissionService.findPermissionCodeList(accountId);

        return permissions;
    }

    /**
     * 核心方法：返回一个账号所拥有的角色集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        if(loginId == null){
            return new ArrayList<>();
        }

        Long accountId = Long.parseLong(loginId.toString());
        List<String> roles = roleService.findRoleCodeList(accountId);

        return roles;
    }


}
