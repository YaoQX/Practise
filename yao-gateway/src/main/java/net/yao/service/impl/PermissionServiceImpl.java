package net.yao.service.impl;

import net.yao.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.yao.service.PermissionService;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    public List<String> findPermissionCodeList(Long accountId)
    {
        return permissionMapper.findPermissionCodeList(accountId);
    }

}
