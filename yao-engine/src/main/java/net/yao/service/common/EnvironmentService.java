package net.yao.service.common;

import net.yao.dto.common.EnvironmentDTO;
import net.yao.req.common.EnvironmentDelReq;
import net.yao.req.common.EnvironmentSaveReq;
import net.yao.req.common.EnvironmentUpdateReq;

import java.util.List;

public interface EnvironmentService{

    public List<EnvironmentDTO> list(Long projectId);

    public int save(EnvironmentSaveReq req);

    public int update(EnvironmentUpdateReq req);

    public int delete(Long projectId, Long id);
}
