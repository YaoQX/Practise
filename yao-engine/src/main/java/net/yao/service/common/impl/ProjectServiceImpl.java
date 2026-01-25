package net.yao.service.common.impl;

import net.yao.dto.common.ProjectDTO;
import net.yao.mapper.ProjectMapper;
import net.yao.model.ProjectDO;
import net.yao.req.common.ProjectDelReq;
import net.yao.req.common.ProjectSaveReq;
import net.yao.req.common.ProjectUpdateReq;
import net.yao.service.common.ProjectService;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    public List<ProjectDTO> list(){
        List<ProjectDO> projectDOList = projectMapper.selectList(null);
        List<ProjectDTO> projectDTOList = SpringBeanUtil.copyProperties(projectDOList,ProjectDTO.class);
        return projectDTOList;

    }

    @Override
    public int save(ProjectSaveReq projectSaveReq) {
        ProjectDO projectDO = SpringBeanUtil.copyProperties(projectSaveReq,ProjectDO.class);
        return projectMapper.insert(projectDO);
    }

    public int update(ProjectUpdateReq projectUpdateReq) {
        ProjectDO projectDO = SpringBeanUtil.copyProperties(projectUpdateReq,ProjectDO.class);
        return projectMapper.updateById(projectDO);
    }

    public int delete(Long id) {
        return projectMapper.deleteById(id);
    }
}
