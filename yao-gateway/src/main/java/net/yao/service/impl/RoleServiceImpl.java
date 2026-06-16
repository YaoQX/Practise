package net.yao.service.impl;

import net.yao.gatewaymapper.RoleMapper;
import net.yao.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<String> findRoleCodeList(Long accountId)
    {
        return roleMapper.findRoleCodeList(accountId);
    }
}
