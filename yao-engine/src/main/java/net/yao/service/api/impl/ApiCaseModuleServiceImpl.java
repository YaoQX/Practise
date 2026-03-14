package net.yao.service.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;


import net.yao.dto.api.ApiCaseDTO;
import net.yao.dto.api.ApiCaseModuleDTO;
import net.yao.mapper.ApiCaseMapper;
import net.yao.mapper.ApiCaseModuleMapper;
import net.yao.mapper.ApiCaseStepMapper;
import net.yao.model.ApiCaseDO;
import net.yao.model.ApiCaseModuleDO;
import net.yao.model.ApiCaseStepDO;
import net.yao.req.api.ApiCaseModuleDelReq;
import net.yao.req.api.ApiCaseModuleSaveReq;
import net.yao.req.api.ApiCaseModuleUpdateReq;
import net.yao.service.api.ApiCaseModuleService;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 小滴课堂,愿景：让技术不再难学
 *
 * @Description
 * @Author 二当家小D
 * @Remark 有问题直接联系我，源码-笔记-技术交流群
 * @Version 1.0
 **/
@Service
public class ApiCaseModuleServiceImpl implements ApiCaseModuleService {

    @Autowired
    private ApiCaseModuleMapper apiCaseModuleMapper;

    @Autowired
    private ApiCaseMapper apiCaseMapper;

    @Autowired
    private ApiCaseStepMapper apiCaseStepMapper;

    /**
     * 根据项目ID获取ApiCaseModuleDTO列表
     */
    @Override
    public List<ApiCaseModuleDTO> list(Long projectId) {
        LambdaQueryWrapper<ApiCaseModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiCaseModuleDO::getProjectId, projectId);
        List<ApiCaseModuleDO> apiCaseModuleDOS = apiCaseModuleMapper.selectList(queryWrapper);
        List<ApiCaseModuleDTO> list = SpringBeanUtil.copyProperties(apiCaseModuleDOS, ApiCaseModuleDTO.class);
        list.forEach(apiCaseModuleDTO -> {
            LambdaQueryWrapper<ApiCaseDO> caseQueryWrapper = new LambdaQueryWrapper<>();
            caseQueryWrapper.eq(ApiCaseDO::getModuleId, apiCaseModuleDTO.getId());
            List<ApiCaseDO> apiCaseDOS = apiCaseMapper.selectList(caseQueryWrapper);
            apiCaseModuleDTO.setList(SpringBeanUtil.copyProperties(apiCaseDOS, ApiCaseDTO.class));
        });
        return list;
    }


    @Override
    public ApiCaseModuleDTO getById(Long projectId, Long moduleId) {
        LambdaQueryWrapper<ApiCaseModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiCaseModuleDO::getProjectId, projectId).eq(ApiCaseModuleDO::getId, moduleId);
        ApiCaseModuleDO apiCaseModuleDO = apiCaseModuleMapper.selectOne(queryWrapper);
        ApiCaseModuleDTO apiCaseModuleDTO = SpringBeanUtil.copyProperties(apiCaseModuleDO, ApiCaseModuleDTO.class);

        //查询模块下的用例列表
        LambdaQueryWrapper<ApiCaseDO> apiCaseQueryWrapper = new LambdaQueryWrapper<>();
        apiCaseQueryWrapper.eq(ApiCaseDO::getModuleId, apiCaseModuleDTO.getId());
        List<ApiCaseDO> apiCaseDOS = apiCaseMapper.selectList(apiCaseQueryWrapper);
        List<ApiCaseDTO> apiCaseDTOS = SpringBeanUtil.copyProperties(apiCaseDOS, ApiCaseDTO.class);
        apiCaseModuleDTO.setList(apiCaseDTOS);
        return apiCaseModuleDTO;
    }

    @Override
    public int save(ApiCaseModuleSaveReq req) {
        ApiCaseModuleDO apiCaseModuleDO = SpringBeanUtil.copyProperties(req, ApiCaseModuleDO.class);
        return apiCaseModuleMapper.insert(apiCaseModuleDO);
    }

    @Override
    public int update(ApiCaseModuleUpdateReq req) {
        ApiCaseModuleDO apiCaseModuleDO = SpringBeanUtil.copyProperties(req, ApiCaseModuleDO.class);
        LambdaQueryWrapper<ApiCaseModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiCaseModuleDO::getId, req.getId()).eq(ApiCaseModuleDO::getProjectId, req.getProjectId());
        return apiCaseModuleMapper.update(apiCaseModuleDO, queryWrapper);
    }

    @Override
    public int del(ApiCaseModuleDelReq req) {
        Long projectId = req.getProjectId();
        Long moduleId = req.getId();

        // 1. 先查模块是否存在（可选，但推荐）
        LambdaQueryWrapper<ApiCaseModuleDO> moduleQueryWrapper = new LambdaQueryWrapper<>(ApiCaseModuleDO.class);
        moduleQueryWrapper.eq(ApiCaseModuleDO::getProjectId, projectId)
                .eq(ApiCaseModuleDO::getId, moduleId);

        ApiCaseModuleDO module = apiCaseModuleMapper.selectOne(moduleQueryWrapper);
        if (module == null) {
            return 0;
        }

        // 2. 查询模块下所有用例ID
        LambdaQueryWrapper<ApiCaseDO> caseQueryWrapper = new LambdaQueryWrapper<>(ApiCaseDO.class);
        caseQueryWrapper.select(ApiCaseDO::getId)
                .eq(ApiCaseDO::getProjectId, projectId)
                .eq(ApiCaseDO::getModuleId, moduleId);

        List<Long> caseIdList = apiCaseMapper.selectList(caseQueryWrapper)
                .stream()
                .map(ApiCaseDO::getId)
                .toList();

        // 3. 先删用例下的步骤
        if (!caseIdList.isEmpty()) {
            LambdaQueryWrapper<ApiCaseStepDO> stepDeleteWrapper = new LambdaQueryWrapper<>(ApiCaseStepDO.class);
            stepDeleteWrapper.in(ApiCaseStepDO::getCaseId, caseIdList);
            apiCaseStepMapper.delete(stepDeleteWrapper);

            // 4. 再删模块下的用例
            LambdaQueryWrapper<ApiCaseDO> caseDeleteWrapper = new LambdaQueryWrapper<>(ApiCaseDO.class);
            caseDeleteWrapper.in(ApiCaseDO::getId, caseIdList);
            apiCaseMapper.delete(caseDeleteWrapper);
        }

        // 5. 最后删模块
        return apiCaseModuleMapper.delete(moduleQueryWrapper);
    }
}
