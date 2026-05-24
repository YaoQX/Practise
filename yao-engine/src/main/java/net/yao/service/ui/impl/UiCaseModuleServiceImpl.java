package net.yao.service.ui.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.yao.dto.ui.UiCaseDTO;
import net.yao.dto.ui.UiCaseModuleDTO;
import net.yao.mapper.UiCaseMapper;
import net.yao.mapper.UiCaseModuleMapper;
import net.yao.mapper.UiCaseStepMapper;
import net.yao.model.UiCaseDO;
import net.yao.model.UiCaseModuleDO;
import net.yao.model.UiCaseStepDO;
import net.yao.req.ui.UiCaseModuleSaveReq;
import net.yao.req.ui.UiCaseModuleUpdateReq;
import net.yao.service.ui.UiCaseModuleService;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UiCaseModuleServiceImpl implements UiCaseModuleService {

    @Autowired
    private UiCaseModuleMapper uiCaseModuleMapper;

    @Autowired
    private UiCaseStepMapper uiCaseStepMapper;

    @Autowired
    private UiCaseMapper uiCaseMapper;

    public int save(UiCaseModuleSaveReq req) {
        UiCaseModuleDO uiCaseModuleDO = SpringBeanUtil.copyProperties(req, UiCaseModuleDO.class);
        return uiCaseModuleMapper.insert(uiCaseModuleDO);
    }

    public int update(UiCaseModuleUpdateReq req){
        UiCaseModuleDO uiCaseModuleDO = SpringBeanUtil.copyProperties(req, UiCaseModuleDO.class);
        LambdaQueryWrapper<UiCaseModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UiCaseModuleDO::getId, uiCaseModuleDO .getId()).eq(UiCaseModuleDO::getProjectId, uiCaseModuleDO .getProjectId());
        return uiCaseModuleMapper.update(uiCaseModuleDO, queryWrapper);
    }

    public int delete(Long id, Long projectId) {
        // 删除模块 一个模块有好几个用例 一个用例好几个步骤
        LambdaQueryWrapper<UiCaseModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UiCaseModuleDO::getId, id).eq(UiCaseModuleDO::getProjectId, projectId);
        int delete = uiCaseModuleMapper.delete(queryWrapper);
        if (delete > 0) {
            // 删除模块下的所有用例
            LambdaQueryWrapper<UiCaseDO> queryWrapperCase = new LambdaQueryWrapper<>();
            queryWrapperCase.select(UiCaseDO::getId).eq(UiCaseDO::getModuleId, id);
            List<Long> caseIdList = uiCaseMapper.selectList(queryWrapperCase).stream().map(UiCaseDO::getId).toList();
            if(!caseIdList.isEmpty()){
                uiCaseMapper.deleteBatchIds(caseIdList);

                // 删除用例下所有步骤
                LambdaQueryWrapper<UiCaseStepDO> queryWrapperStep = new LambdaQueryWrapper<>();
                queryWrapperStep.select(UiCaseStepDO::getId).in(UiCaseStepDO::getCaseId, caseIdList);
                List<Long> stepIdList = uiCaseStepMapper.selectList(queryWrapperStep).stream().map(UiCaseStepDO::getId).toList();
                if(!stepIdList.isEmpty()){
                    uiCaseStepMapper.deleteBatchIds(stepIdList);
                }
            }

      }
        return delete;
    }

    /**
     * 根据项目找所有模块
     */
    public List<UiCaseModuleDTO> list(Long projectId) {
        LambdaQueryWrapper<UiCaseModuleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UiCaseModuleDO::getProjectId, projectId);
        List<UiCaseModuleDO> list = uiCaseModuleMapper.selectList(queryWrapper);

        // 如果一个模块都没有，直接返回空列表，省得往下跑了
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        // 转换为 DTO 列表
        List<UiCaseModuleDTO> uiCaseModuleDTOS = SpringBeanUtil.copyProperties(list, UiCaseModuleDTO.class);

        // ================== 🔥 下面是干掉 for 循环查库的绝招 ==================

        // 2. ⚡ 提取出所有模块的 ID 集合 (比如得到 [1, 2, 3])
        List<Long> moduleIdList = uiCaseModuleDTOS.stream()
                .map(UiCaseModuleDTO::getId)
                .toList();

        // 3. ⚡ 【第 2 次查库】利用 IN 关键字，一次性把这些模块下的所有用例全捞出来
        LambdaQueryWrapper<UiCaseDO> caseQueryWrapper = new LambdaQueryWrapper<>(UiCaseDO.class);
        caseQueryWrapper.in(UiCaseDO::getModuleId, moduleIdList); // 相当于 WHERE module_id IN (1, 2, 3)
        List<UiCaseDO> uiCaseDOS = uiCaseMapper.selectList(caseQueryWrapper);

        // 转换为用例的 DTO 列表
        List<UiCaseDTO> uiCaseDTOS = SpringBeanUtil.copyProperties(uiCaseDOS, UiCaseDTO.class);

        // 4. 🎯 【核心魔法】在内存中，把这堆乱糟糟的用例，按照“模块ID”进行分组，做成一个 Map
        // 这个 Map 的 Key 是模块ID，Value 是属于这个模块的用例列表 List<UiCaseDTO>   要把用例装进对应的模块大箱子
        Map<Long, List<UiCaseDTO>> caseMapByModuleId = uiCaseDTOS.stream()
                .collect(Collectors.groupingBy(UiCaseDTO::getModuleId));

        // 5. 🔄 此时我们依然可以用 for 循环，但循环里面【绝对不查数据库】了，只做纯内存赋值！
        for (UiCaseModuleDTO uiCaseModuleDTO : uiCaseModuleDTOS) {
            Long moduleId = uiCaseModuleDTO.getId();

            // 直接去刚才分好类的 Map 里拿数据，拿到了就塞进去；如果这个模块刚好没用例，就给个空列表
            List<UiCaseDTO> currentModuleCases = caseMapByModuleId.getOrDefault(moduleId, new ArrayList<>());
            uiCaseModuleDTO.setList(currentModuleCases);
        }

        return uiCaseModuleDTOS;


    }

    /**
     * 根据模块ID和项目ID，获取模块详情
     */
    public UiCaseModuleDTO getById(Long projectId, Long moduleId) {

        LambdaQueryWrapper<UiCaseModuleDO> queryWrapper = new LambdaQueryWrapper<>(UiCaseModuleDO.class);
        queryWrapper.eq(UiCaseModuleDO::getProjectId, projectId).eq(UiCaseModuleDO::getId, moduleId);

        // 1. 精准查出这【一个】模块的名字、描述等基础信息
        UiCaseModuleDO uiCaseModuleDO = uiCaseModuleMapper.selectOne(queryWrapper);
        UiCaseModuleDTO uiCaseModuleDTO = SpringBeanUtil.copyProperties(uiCaseModuleDO, UiCaseModuleDTO.class);

        // 2. 既然进到了这个模块的详情页，顺便去把属于这个模块的【所有用例】也捞出来
        LambdaQueryWrapper<UiCaseDO> caseQueryWapper = new LambdaQueryWrapper<>(UiCaseDO.class);
        List<UiCaseDO> uiCaseDOS = uiCaseMapper.selectList(caseQueryWapper);
        List<UiCaseDTO> uiCaseDTOS = SpringBeanUtil.copyProperties(uiCaseDOS, UiCaseDTO.class);

        // 3. 把用例塞进这个模块里
        uiCaseModuleDTO.setList(uiCaseDTOS);

        // 4. 把这个丰满的、带用例列表的【单个模块详情对象】交差返回
        return uiCaseModuleDTO;
    }






}
