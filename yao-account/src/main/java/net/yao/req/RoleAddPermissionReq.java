package net.yao.req;

import lombok.Data;

@Data
public class RoleAddPermissionReq {

    private Long roleId;

    private Long permissionId;
}
