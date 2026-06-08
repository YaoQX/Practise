package net.yao.service;

import java.util.List;

public interface RoleService {

    List<String> findRoleCodeList(Long accountId);
}
