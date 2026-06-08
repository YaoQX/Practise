package net.yao.service;

import net.yao.dto.AccountDTO;
import net.yao.dto.RoleDTO;
import net.yao.req.*;

import java.util.List;

public interface RoleService {

    int addPermission(RoleAddPermissionReq req);

    int delPermission(RoleDelPermissionReq req);

    int addRoleByAccountId(AccountRoleAddReq req);

    int delRoleByAccountId(AccountRoleDelReq req);

    List<RoleDTO> list();

    int addRole(RoleAddReq addReq);

    int deleteRole(Long id);

    AccountDTO getAccountWithRoleByAccountId(Long accountId);
}
