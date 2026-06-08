package net.yao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.yao.dto.AccountDTO;
import net.yao.dto.RoleDTO;
import net.yao.mapper.RoleMapper;
import net.yao.mapper.RolePermissionMapper;
import net.yao.mapper.AccountRoleMapper;
import net.yao.model.AccountRoleDO;
import net.yao.model.RoleDO;
import net.yao.model.RolePermissionDO;
import net.yao.req.*;
import net.yao.service.RoleService;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private AccountRoleMapper accountRoleMapper;

    /**
     * 给某个角色赋予某个权限
     */
    public int addPermission(RoleAddPermissionReq req)
    {
        LambdaQueryWrapper<RolePermissionDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermissionDO::getRoleId, req.getRoleId())
                .eq(RolePermissionDO::getPermissionId, req.getPermissionId());

        Long count = rolePermissionMapper.selectCount(queryWrapper);
        //防止重复插入相同的权限
        if(count == 0)
        {
            RolePermissionDO rolePermissionDO = new RolePermissionDO();
            rolePermissionDO.setRoleId(req.getRoleId());
            rolePermissionDO.setPermissionId(req.getPermissionId());
            return rolePermissionMapper.insert(rolePermissionDO);
        }
        return 0;
    }

    public int delPermission(RoleDelPermissionReq req)
    {
        LambdaQueryWrapper<RolePermissionDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RolePermissionDO::getRoleId, req.getRoleId())
                .eq(RolePermissionDO::getPermissionId, req.getPermissionId());
        return rolePermissionMapper.delete(queryWrapper);
    }

    /**
     * 给某个账号添加某个角色
     */
    public int addRoleByAccountId(AccountRoleAddReq req)
    {
        LambdaQueryWrapper<AccountRoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AccountRoleDO::getAccountId, req.getAccountId())
                .eq(AccountRoleDO::getRoleId, req.getRoleId());
        if(accountRoleMapper.selectCount(queryWrapper) == 0){
            AccountRoleDO accountRoleDO = new AccountRoleDO();
            accountRoleDO.setAccountId(req.getAccountId());
            accountRoleDO.setRoleId(req.getRoleId());
            return accountRoleMapper.insert(accountRoleDO);
        }
        return 0;
    }

    public int delRoleByAccountId(AccountRoleDelReq req)
    {
        LambdaQueryWrapper<AccountRoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AccountRoleDO::getAccountId, req.getAccountId())
                .eq(AccountRoleDO::getRoleId, req.getRoleId());
        return accountRoleMapper.delete(queryWrapper);
    }

    public List<RoleDTO> list()
    {

        List<RoleDTO> list = roleMapper.listRoleWithPermission();

        return list;
    }


    public int addRole(RoleAddReq addReq)
    {
        RoleDO roleDO = SpringBeanUtil.copyProperties(addReq, RoleDO.class);
        return roleMapper.insert(roleDO);
    }


    public int deleteRole(Long id)
    {
        int rows = roleMapper.deleteById(id);
        if(rows>0){
            accountRoleMapper.delete(new LambdaQueryWrapper<AccountRoleDO>().eq(AccountRoleDO::getRoleId,id));
            rolePermissionMapper.delete(new LambdaQueryWrapper<RolePermissionDO>().eq(RolePermissionDO::getRoleId,id));
        }
        return rows;
    }

    @Override
    public AccountDTO getAccountWithRoleByAccountId(Long accountId)
    {
        AccountDTO accountDTO = roleMapper.findAccountWithRoleAndPermission(accountId);
        return accountDTO;
    }


}
