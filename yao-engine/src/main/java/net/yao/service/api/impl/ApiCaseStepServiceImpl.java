package net.yao.service.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.yao.mapper.ApiCaseStepMapper;
import net.yao.model.ApiCaseStepDO;
import net.yao.req.api.ApiCaseStepSaveReq;
import net.yao.req.api.ApiCaseStepUpdateReq;
import net.yao.service.api.ApiCaseStepService;
import net.yao.service.api.ApiModuleService;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiCaseStepServiceImpl implements ApiCaseStepService {
    @Autowired
    private ApiCaseStepMapper apiCaseStepMapper;


    public int save(ApiCaseStepSaveReq req) {
        ApiCaseStepDO apiCaseStepDO = SpringBeanUtil.copyProperties(req, ApiCaseStepDO.class);
        return apiCaseStepMapper.insert(apiCaseStepDO);
    }


    public int update(ApiCaseStepUpdateReq req) {
        ApiCaseStepDO apiCaseStepDO = SpringBeanUtil.copyProperties(req, ApiCaseStepDO.class);
        LambdaQueryWrapper<ApiCaseStepDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiCaseStepDO::getId, req.getId()).eq(ApiCaseStepDO::getProjectId,req.getProjectId());
        return apiCaseStepMapper.update(apiCaseStepDO,queryWrapper);
    }


    public int del(Long projectId, Long id) {
        LambdaQueryWrapper<ApiCaseStepDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiCaseStepDO::getId, id).eq(ApiCaseStepDO::getProjectId,projectId);
        return apiCaseStepMapper.delete(queryWrapper);
    }

}
