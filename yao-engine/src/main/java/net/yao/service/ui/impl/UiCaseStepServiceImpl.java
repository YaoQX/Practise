package net.yao.service.ui.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.yao.mapper.UiCaseStepMapper;
import net.yao.model.UiCaseStepDO;
import net.yao.req.ui.UiCaseStepDelReq;
import net.yao.req.ui.UiCaseStepSaveReq;
import net.yao.req.ui.UiCaseStepUpdateReq;
import net.yao.service.ui.UiCaseStepService;
import net.yao.util.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UiCaseStepServiceImpl implements UiCaseStepService {

    @Autowired
    private UiCaseStepMapper uiCaseStepMapper;

    public int save(UiCaseStepSaveReq req)
    {
        return uiCaseStepMapper.insert(SpringBeanUtil.copyProperties(req, UiCaseStepDO.class));
    }

    public int update(UiCaseStepUpdateReq req)
    {
        // 1. 属性拷贝
        UiCaseStepDO uiCaseStepDO = SpringBeanUtil.copyProperties(req, UiCaseStepDO.class);

        // 2. 构造更新条件
        LambdaQueryWrapper<UiCaseStepDO> queryWrapper = new LambdaQueryWrapper<>(UiCaseStepDO.class);

        // 🎯 修复核心：锁定 步骤ID 和 它所属的用例ID（caseId），这样才匹配表结构，同时也能防越权
        queryWrapper.eq(UiCaseStepDO::getId, uiCaseStepDO.getId());

        if (req.getCaseId() != null) {
            queryWrapper.eq(UiCaseStepDO::getCaseId, req.getCaseId());
        }

        // 3. 执行更新
        return uiCaseStepMapper.update(uiCaseStepDO, queryWrapper);
    }

    public int delete(UiCaseStepDelReq req) {
        LambdaQueryWrapper<UiCaseStepDO> queryWrapper = new LambdaQueryWrapper<>(UiCaseStepDO.class);
        queryWrapper.eq(UiCaseStepDO::getId, req.getId()).eq(UiCaseStepDO::getProjectId, req.getProjectId());
        return uiCaseStepMapper.delete(queryWrapper);
    }



}
