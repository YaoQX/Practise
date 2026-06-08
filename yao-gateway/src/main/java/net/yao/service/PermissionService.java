package net.yao.service;

import java.util.List;

public interface PermissionService {

    List<String> findPermissionCodeList(Long accountId);
}
