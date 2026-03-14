package net.yao.service.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.yao.dto.api.ApiDTO;
import net.yao.mapper.ApiMapper;
import net.yao.model.ApiDO;
import net.yao.req.api.ApiDelReq;
import net.yao.req.api.ApiSaveReq;
import net.yao.req.api.ApiUpdateReq;
import net.yao.service.api.ApiService;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    private ApiMapper apiMapper;


    public ApiDTO getById(Long projectId, Long id) {
        //根据projectId和id查找api对象
        LambdaQueryWrapper<ApiDO> queryWrapper = new LambdaQueryWrapper<ApiDO>();
        queryWrapper.eq(ApiDO::getProjectId, projectId).eq(ApiDO::getId, id);
        ApiDO apiDO = apiMapper.selectOne(queryWrapper);
        return SpringBeanUtil.copyProperties(apiDO, ApiDTO.class);
    }

    public int save(ApiSaveReq req) {
        ApiDO apiDO = SpringBeanUtil.copyProperties(req, ApiDO.class);
        return apiMapper.insert(apiDO);
    }

    public int update(ApiUpdateReq req) {
        ApiDO apiDO = SpringBeanUtil.copyProperties(req, ApiDO.class);
        LambdaQueryWrapper<ApiDO> queryWrapper = new LambdaQueryWrapper<ApiDO>();
        queryWrapper.eq(ApiDO::getProjectId, apiDO.getProjectId()).eq(ApiDO::getId, apiDO.getId());
        return apiMapper.update(apiDO,queryWrapper);
    }

    public int delete(ApiDelReq req) {
        LambdaQueryWrapper<ApiDO> queryWrapper = new LambdaQueryWrapper<ApiDO>();
        queryWrapper.eq(ApiDO::getProjectId, req.getProjectId()).eq(ApiDO::getId, req.getId());
        return apiMapper.delete(queryWrapper);
    }


}
