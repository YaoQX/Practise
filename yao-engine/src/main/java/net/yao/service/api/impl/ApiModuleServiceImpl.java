package net.yao.service.api.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.yao.dto.api.ApiDTO;
import net.yao.dto.api.ApiModuleDTO;
import net.yao.mapper.ApiMapper;
import net.yao.mapper.ApiModuleMapper;
import net.yao.model.ApiDO;
import net.yao.model.ApiModuleDO;
import net.yao.req.api.ApiModuleDelReq;
import net.yao.req.api.ApiModuleSaveReq;
import net.yao.req.api.ApiModuleUpdateReq;
import net.yao.service.api.ApiModuleService;
import net.yao.util.JsonData;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class ApiModuleServiceImpl implements ApiModuleService {

    @Autowired
    private ApiModuleMapper apiModuleMapper;

    @Autowired
    private ApiMapper apiMapper;

    /**
     * 获取指定项目的所有API模块及其关联的API列表
     *
     * @param projectId 项目 ID
     * @return API模块DTO列表，每个模块包含其关联的API列表
     */
    public List<ApiModuleDTO> list(Long projectId){
        // 构建查询条件，按项目ID过滤API模块
        LambdaQueryWrapper<ApiModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiModuleDO::getProjectId, projectId);

        // 查询项目下的所有API模块并转换为DTO
        List<ApiModuleDO> apiModuleDOList = apiModuleMapper.selectList(queryWrapper);
        List<ApiModuleDTO> list = SpringBeanUtil.copyProperties(apiModuleDOList, ApiModuleDTO.class);

        // 为每个API模块加载其关联的API列表
        list.forEach(apiModuleDTO -> {
            // 构建查询条件，按当前模块ID过滤API并按API ID降序排序
            LambdaQueryWrapper<ApiDO> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(ApiDO::getModuleId, apiModuleDTO.getId()).orderByDesc(ApiDO::getId);
            // 查询模块下的所有API并转换为DTO
            List<ApiDO> apiDOList = apiMapper.selectList(queryWrapper1);
            apiModuleDTO.setList(SpringBeanUtil.copyProperties(apiDOList, ApiDTO.class));
        });
        return list;
    }

    /**
     * 根据ID查找
     */
    public ApiModuleDTO getById(Long projectId, Long moduleId){
        LambdaQueryWrapper<ApiModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiModuleDO::getProjectId, projectId).eq(ApiModuleDO::getId, moduleId);

        ApiModuleDO apiModuleDO = apiModuleMapper.selectOne(queryWrapper);

        ApiModuleDTO apiModuleDTO = SpringBeanUtil.copyProperties(apiModuleDO, ApiModuleDTO.class);

        //查询模块下面关联接口
        if (apiModuleDTO != null) {
            LambdaQueryWrapper<ApiDO> apiQueryWrapper = new LambdaQueryWrapper<>();
            apiQueryWrapper.eq(ApiDO::getModuleId, apiModuleDTO.getId()).orderByDesc(ApiDO::getId);
            List<ApiDO> apiDOS = apiMapper.selectList(apiQueryWrapper);
            apiModuleDTO.setList(SpringBeanUtil.copyProperties(apiDOS, ApiDTO.class));
        }
        return apiModuleDTO;
    }

    public int delete(Long id, Long projectId) {

        LambdaQueryWrapper<ApiModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiModuleDO::getProjectId, projectId).eq(ApiModuleDO::getId, id);
        //删除模块
        int deleted = apiModuleMapper.delete(queryWrapper);
        if (deleted != 0) {
            //删除模块下面的api
            LambdaQueryWrapper<ApiDO> apiQueryWrapper = new LambdaQueryWrapper<>();
            apiQueryWrapper.eq(ApiDO::getModuleId, id).eq(ApiDO::getProjectId, projectId);
            apiMapper.delete(apiQueryWrapper);
        }
        return deleted;

    }

    public int update(ApiModuleUpdateReq req){
        ApiModuleDO apiModuleDO = SpringBeanUtil.copyProperties(req, ApiModuleDO.class);
        LambdaQueryWrapper<ApiModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiModuleDO::getProjectId, apiModuleDO.getProjectId()).eq(ApiModuleDO::getId, apiModuleDO.getId());
        return apiModuleMapper.update(apiModuleDO,queryWrapper);
    }

    public int save(ApiModuleSaveReq req){
        ApiModuleDO apiModuleDO = SpringBeanUtil.copyProperties(req, ApiModuleDO.class);
        return apiModuleMapper.insert(apiModuleDO);
    }
}
