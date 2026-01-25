package net.yao.service.common.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.yao.dto.common.EnvironmentDTO;
import net.yao.mapper.EnvironmentMapper;
import net.yao.mapper.ProjectMapper;
import net.yao.model.EnvironmentDO;
import net.yao.model.ProjectDO;
import net.yao.req.common.EnvironmentDelReq;
import net.yao.req.common.EnvironmentSaveReq;
import net.yao.req.common.EnvironmentUpdateReq;
import net.yao.service.common.EnvironmentService;
import net.yao.util.JsonData;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class EnvironmentServiceImpl implements EnvironmentService {

    @Autowired
    private EnvironmentMapper environmentMapper;

    @Autowired //先看项目存在否
    private ProjectMapper projectMapper;

    public List<EnvironmentDTO> list(Long projectId){
        LambdaQueryWrapper<EnvironmentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnvironmentDO::getProjectId, projectId);
        List<EnvironmentDO> list = environmentMapper.selectList(wrapper);
        return SpringBeanUtil.copyProperties(list, EnvironmentDTO.class);
    }

    public int save(EnvironmentSaveReq req){
        ProjectDO projectDO = projectMapper.selectById(req.getProjectId());
        if (projectDO != null){
            EnvironmentDO environmentDO = SpringBeanUtil.copyProperties(req,EnvironmentDO.class);
            return environmentMapper.insert(environmentDO);
        }
        return 0;
    }

    @Override
    public int update(EnvironmentUpdateReq req) {
        ProjectDO projectDO = projectMapper.selectById(req.getProjectId());
        if (projectDO !=null){
            EnvironmentDO environmentDO = SpringBeanUtil.copyProperties(req, EnvironmentDO.class);
            return environmentMapper.updateById(environmentDO);
        }
        return 0;
    }

    @Override
    public int delete(Long projectId, Long id) {
        LambdaQueryWrapper<EnvironmentDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnvironmentDO::getProjectId,projectId);
        queryWrapper.eq(EnvironmentDO::getId,id);
        return environmentMapper.delete(queryWrapper);
    }




}
