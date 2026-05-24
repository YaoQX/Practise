package net.yao.service.ui.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.yao.dto.ReportDTO;
import net.yao.dto.UiCaseStepDTO;
import net.yao.dto.common.CaseInfoDTO;
import net.yao.dto.ui.UiCaseDTO;
import net.yao.enums.ReportStateEnum;
import net.yao.enums.TestTypeEnum;
import net.yao.feign.ReportFeignService;
import net.yao.mapper.UiCaseMapper;
import net.yao.mapper.UiCaseStepMapper;
import net.yao.model.UiCaseDO;
import net.yao.model.UiCaseStepDO;
import net.yao.req.ReportSaveReq;
import net.yao.req.ui.UiCaseDelReq;
import net.yao.req.ui.UiCaseSaveReq;
import net.yao.req.ui.UiCaseUpdateReq;
import net.yao.service.ui.UiCaseService;
import net.yao.util.JsonData;
import net.yao.util.SpringBeanUtil;
import org.checkerframework.checker.units.qual.A;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class UiCaseServiceImpl implements UiCaseService {

    @Autowired
    private UiCaseMapper uiCaseMapper;

    @Autowired
    private UiCaseStepMapper uiCaseStepMapper;

    private ReportFeignService reportFeignService;

    /**
     * 查询用例关联步骤
     */
    private List<UiCaseStepDO> getStepList(Long caseId)
    {
        LambdaQueryWrapper<UiCaseStepDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UiCaseStepDO::getCaseId, caseId).orderByAsc(UiCaseStepDO::getNum);
        return uiCaseStepMapper.selectList(queryWrapper);
    }

    public UiCaseDTO find(Long projectId, Long caseId)
    {
        LambdaQueryWrapper<UiCaseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UiCaseDO::getId, caseId).eq(UiCaseDO::getProjectId, projectId);
        UiCaseDO uiCaseDO = uiCaseMapper.selectOne(queryWrapper);

        UiCaseDTO uiCaseDTO = SpringBeanUtil.copyProperties(uiCaseDO, UiCaseDTO.class);

        List<UiCaseStepDO> stepList = getStepList(caseId);
        uiCaseDTO.setList(SpringBeanUtil.copyProperties(stepList, UiCaseStepDTO.class));
        return uiCaseDTO;
    }

    public int delete(UiCaseDelReq req)
    {
        // 删除用例
        LambdaQueryWrapper<UiCaseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UiCaseDO::getId, req.getId()).eq(UiCaseDO::getProjectId, req.getProjectId());
        int delete = uiCaseMapper.delete(queryWrapper);
        if (delete > 0)
        {
            // 删除用例下的所有步骤
            LambdaQueryWrapper<UiCaseStepDO> queryWrapperStep = new LambdaQueryWrapper<>();
            queryWrapperStep.eq(UiCaseStepDO::getCaseId, req.getId());
            uiCaseStepMapper.delete(queryWrapperStep);
        }
        return  delete;
    }

    public int update(UiCaseUpdateReq req)
    {
        UiCaseDO uiCaseDO = SpringBeanUtil.copyProperties(req, UiCaseDO.class);

        LambdaQueryWrapper<UiCaseDO> queryWrapper = new LambdaQueryWrapper<>(UiCaseDO.class);
        queryWrapper.eq(UiCaseDO::getId, req.getId()).eq(UiCaseDO::getProjectId, req.getProjectId());
        return uiCaseMapper.update(uiCaseDO, queryWrapper);

    }

    @Transactional(rollbackFor = Exception.class) //事务，保证数据要么全有，要么全无
    public int save(UiCaseSaveReq req)
    {
        UiCaseDO uiCaseDO = SpringBeanUtil.copyProperties(req, UiCaseDO.class);
        int insert = uiCaseMapper.insert(uiCaseDO);
        if(req.getList()!=null){
            // 添加了若干个测试步骤
            for (int i = 0; i < req.getList().size(); i++) {
                UiCaseStepDO uiCaseStepDO = SpringBeanUtil.copyProperties(req.getList().get(i), UiCaseStepDO.class);
                uiCaseStepDO.setCaseId(uiCaseDO.getId());
                uiCaseStepDO.setNum(Long.valueOf(i+1));
                uiCaseStepMapper.insert(uiCaseStepDO);
            }
        }
        return insert;
    }

    public JsonData execute(Long projectId, Long caseId)
    {
        // 查找用例
        LambdaQueryWrapper<UiCaseDO> queryCaseWrapper = new LambdaQueryWrapper<>();
        queryCaseWrapper.eq(UiCaseDO::getId, caseId).eq(UiCaseDO::getProjectId, projectId);
        UiCaseDO uiCaseDO = uiCaseMapper.selectOne(queryCaseWrapper);

        if(uiCaseDO==null){
            return JsonData.buildError("Ui Case not exists");
        }

        // 查找用例步骤
        List<UiCaseStepDO> stepList = getStepList(caseId);
        if (stepList.isEmpty()) {
            return JsonData.buildError("Ui Case Step not exists");
        }

        // 测试报告
        ReportSaveReq reportSaveReq = ReportSaveReq.builder().projectId(uiCaseDO.getProjectId())
                                                              .caseId(uiCaseDO.getId())
                                                              .startTime(System.currentTimeMillis())
                                                               .executeState(ReportStateEnum.EXECUTING.name())
                                                              .name(uiCaseDO.getName())
                                                              .type(TestTypeEnum.UI.name()).build();

        JsonData jsonData = reportFeignService.save(reportSaveReq);
        if(jsonData.isSuccess()){

            ReportDTO reportDTO = jsonData.getData(ReportDTO.class);
            CaseInfoDTO caseInfoDTO = new CaseInfoDTO();
            caseInfoDTO.setId(uiCaseDO.getId());
            caseInfoDTO.setModuleId(uiCaseDO.getModuleId());
            caseInfoDTO.setName(uiCaseDO.getName());

            UiExecuteEngine

        }
        else{
            log.error("Initializing the test UI report failed, reason:：{}",jsonData.getMsg());
            return JsonData.buildError("Initializing the test UI report failed");
        }





    }

}
