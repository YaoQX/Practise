package net.yao.service.impl;

import net.yao.dto.PermissionDTO;
import net.yao.mapper.PermissionMapper;
import net.yao.model.PermissionDO;
import net.yao.service.PermissionService;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<PermissionDTO> list() {
        List<PermissionDO> permissionDOS = permissionMapper.selectList(null);
        List<PermissionDTO> permissionDTOS = SpringBeanUtil.copyProperties(permissionDOS, PermissionDTO.class);
        return permissionDTOS;
    }
}
