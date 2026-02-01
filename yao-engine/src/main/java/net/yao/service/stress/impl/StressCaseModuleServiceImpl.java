package net.yao.service.stress.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.yao.dto.stress.StressCaseDTO;
import net.yao.dto.stress.StressCaseModuleDTO;
import net.yao.mapper.StressCaseMapper;
import net.yao.mapper.StressCaseModuleMapper;
import net.yao.model.StressCaseDO;
import net.yao.model.StressCaseModuleDO;
import net.yao.req.stress.StressCaseModuleSaveReq;
import net.yao.req.stress.StressCaseModuleUpdateReq;
import net.yao.service.stress.StressCaseModuleService;
import net.yao.service.stress.StressCaseService;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StressCaseModuleServiceImpl implements StressCaseModuleService {

    @Autowired
    private StressCaseModuleMapper stressCaseModuleMapper;

    @Autowired
    private StressCaseMapper stressCaseMapper;


    public List<StressCaseModuleDTO> list(Long projectId) {
        // 【第一步：查父级】构造查询条件：只查当前项目下的模块
        LambdaQueryWrapper<StressCaseModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StressCaseModuleDO::getProjectId, projectId);
        // 执行查询，得到模块列表 (DO对象)
        List<StressCaseModuleDO> stressCaseModuleDOS = stressCaseModuleMapper.selectList(queryWrapper);
        List<StressCaseModuleDTO> list = SpringBeanUtil.copyProperties(stressCaseModuleDOS, StressCaseModuleDTO.class);
        list.forEach(source -> {
            //查询压测模型下的关联用例
            // 构造子查询：查这个模块ID下的所有用例,排序
            LambdaQueryWrapper<StressCaseDO> caseQueryWrapper = new LambdaQueryWrapper<>();
            caseQueryWrapper.eq(StressCaseDO::getModuleId, source.getId()).orderByDesc(StressCaseDO::getId);
            // 执行查询，得到该模块下的用例
            List<StressCaseDO> stressCaseDOS = stressCaseMapper.selectList(caseQueryWrapper);
            List<StressCaseDTO> stressCaseDTOS = SpringBeanUtil.copyProperties(stressCaseDOS, StressCaseDTO.class);
            source.setList(stressCaseDTOS);
        });
        return list;
    }


    public StressCaseModuleDTO findById(Long projectId, Long moduleId) {
        LambdaQueryWrapper<StressCaseModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StressCaseModuleDO::getProjectId, projectId).eq(StressCaseModuleDO::getId, moduleId);
        StressCaseModuleDO stressCaseModuleDO = stressCaseModuleMapper.selectOne(queryWrapper);
        if (stressCaseModuleDO == null) {
            return null;
        }
        StressCaseModuleDTO stressCaseModuleDTO = SpringBeanUtil.copyProperties(stressCaseModuleDO, StressCaseModuleDTO.class);

        //查询压测模型下的关联用例
        LambdaQueryWrapper<StressCaseDO> caseQueryWrapper = new LambdaQueryWrapper<>();
        caseQueryWrapper.eq(StressCaseDO::getModuleId, moduleId).orderByDesc(StressCaseDO::getId);
        List<StressCaseDO> stressCaseDOS = stressCaseMapper.selectList(caseQueryWrapper);
        List<StressCaseDTO> stressCaseDTOS = SpringBeanUtil.copyProperties(stressCaseDOS, StressCaseDTO.class);
        stressCaseModuleDTO.setList(stressCaseDTOS);
        return stressCaseModuleDTO;
    }


    public int delete(Long projectId, Long id) {
        LambdaQueryWrapper<StressCaseModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StressCaseModuleDO::getProjectId, projectId).eq(StressCaseModuleDO::getId, id);
        int delete = stressCaseModuleMapper.delete(queryWrapper);
        //删除模块下的关联用例
        if (delete > 0) {
            LambdaQueryWrapper<StressCaseDO> caseQueryWrapper = new LambdaQueryWrapper<>();
            caseQueryWrapper.eq(StressCaseDO::getModuleId, id);
            stressCaseMapper.delete(caseQueryWrapper);
        }
        return delete;
    }

    public int save(StressCaseModuleSaveReq req) {
        StressCaseModuleDO stressCaseModuleDO = SpringBeanUtil.copyProperties(req, StressCaseModuleDO.class);
        return stressCaseModuleMapper.insert(stressCaseModuleDO);
    }

    public int update(StressCaseModuleUpdateReq req) {
        StressCaseModuleDO stressCaseModuleDO = SpringBeanUtil.copyProperties(req, StressCaseModuleDO.class);
        LambdaQueryWrapper<StressCaseModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StressCaseModuleDO::getProjectId, stressCaseModuleDO.getProjectId())
                .eq(StressCaseModuleDO::getId, stressCaseModuleDO.getId());
        return stressCaseModuleMapper.update(stressCaseModuleDO, queryWrapper);
    }




}
