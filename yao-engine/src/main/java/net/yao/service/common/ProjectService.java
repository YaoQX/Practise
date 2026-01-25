package net.yao.service.common;

import net.yao.dto.common.ProjectDTO;
import net.yao.mapper.ProjectMapper;
import net.yao.req.common.ProjectDelReq;
import net.yao.req.common.ProjectSaveReq;
import net.yao.req.common.ProjectUpdateReq;

import java.util.List;

public interface ProjectService {


    /**
     * 获取用户项目列表
     * @return
     */
    public List<ProjectDTO> list();

    public int save(ProjectSaveReq projectSaveReq);

    public int update(ProjectUpdateReq projectUpdateReq);

    public int delete(Long id);

}
