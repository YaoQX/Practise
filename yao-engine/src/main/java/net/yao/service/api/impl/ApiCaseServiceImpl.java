package net.yao.service.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.yao.dto.ApiCaseResultDTO;
import net.yao.dto.ReportDTO;
import net.yao.dto.api.ApiCaseDTO;
import net.yao.dto.api.ApiCaseStepDTO;
import net.yao.dto.common.CaseInfoDTO;
import net.yao.enums.BizCodeEnum;
import net.yao.enums.ReportStateEnum;
import net.yao.enums.TestTypeEnum;
import net.yao.exception.BizException;
import net.yao.feign.ReportFeignService;
import net.yao.mapper.ApiCaseMapper;
import net.yao.mapper.ApiCaseStepMapper;
import net.yao.model.ApiCaseDO;
import net.yao.model.ApiCaseStepDO;
import net.yao.req.ReportSaveReq;
import net.yao.req.api.ApiCaseSaveReq;
import net.yao.req.api.ApiCaseUpdateReq;
import net.yao.service.api.ApiCaseService;
import net.yao.service.api.core.ApiExecuteEngine;
import net.yao.util.JsonData;
import net.yao.util.SpringBeanUtil;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class ApiCaseServiceImpl implements ApiCaseService {

    @Resource
    private ApiCaseMapper apiCaseMapper;

    @Resource
    private ApiCaseStepMapper apiCaseStepMapper;

    @Resource
    private ReportFeignService reportFeignService;


    public ApiCaseDTO getById(Long projectId, Long id) {
        LambdaQueryWrapper<ApiCaseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApiCaseDO::getProjectId, projectId)
                .eq(ApiCaseDO::getId, id);
        ApiCaseDO apiCaseDO = apiCaseMapper.selectOne(wrapper);
        ApiCaseDTO apiCaseDTO = SpringBeanUtil.copyProperties(apiCaseDO, ApiCaseDTO.class);

        //顺藤摸瓜：拿着刚才的用例 id，去 api_case_step 表里把属于这个用例的所有“测试步骤”查出来
        LambdaQueryWrapper<ApiCaseStepDO> caseStepQueryWrapper = new LambdaQueryWrapper<ApiCaseStepDO>();
        caseStepQueryWrapper.eq(ApiCaseStepDO::getCaseId, apiCaseDO.getId()).orderByAsc(ApiCaseStepDO::getNum);
        List<ApiCaseStepDO> apiCaseStepDOS = apiCaseStepMapper.selectList(caseStepQueryWrapper);
        List<ApiCaseStepDTO> apiCaseStepDTOS = SpringBeanUtil.copyProperties(apiCaseStepDOS, ApiCaseStepDTO.class);

        // 3. 把步骤列表塞进用例对象里，打包返回给前端
        apiCaseDTO.setList(apiCaseStepDTOS);
        return apiCaseDTO;
    }

    public int save(ApiCaseSaveReq req) {
        ApiCaseDO apiCaseDO = SpringBeanUtil.copyProperties(req, ApiCaseDO.class);
        int insert = apiCaseMapper.insert(apiCaseDO);
        // 保存【用例底下的所有步骤表】 一个用例包含多个步骤
        req.getList().forEach(item -> {
            ApiCaseStepDO apiCaseStepDO = SpringBeanUtil.copyProperties(item, ApiCaseStepDO.class);
            apiCaseStepDO.setCaseId(apiCaseDO.getId());
            apiCaseStepMapper.insert(apiCaseStepDO);
        });

        return insert;
    }

    public int update(ApiCaseUpdateReq req){
        ApiCaseDO apiCaseDO = SpringBeanUtil.copyProperties(req, ApiCaseDO.class);
        LambdaQueryWrapper<ApiCaseDO> queryWrapper = new LambdaQueryWrapper<ApiCaseDO>();
        queryWrapper.eq(ApiCaseDO::getId, req.getId()).eq(ApiCaseDO::getProjectId, req.getProjectId());
        return  apiCaseMapper.update(apiCaseDO, queryWrapper);
    }

    public int del(Long projectId,Long id){
        LambdaQueryWrapper<ApiCaseDO> queryWrapper = new LambdaQueryWrapper<ApiCaseDO>();
        queryWrapper.eq(ApiCaseDO::getId, id).eq(ApiCaseDO::getProjectId, projectId);
        int delete = apiCaseMapper.delete(queryWrapper);

        // 删除用例下步骤
        LambdaQueryWrapper<ApiCaseStepDO> stepQueryWrapper = new LambdaQueryWrapper<ApiCaseStepDO>();
        stepQueryWrapper.eq(ApiCaseStepDO::getCaseId, id);
        apiCaseStepMapper.delete(stepQueryWrapper);
        return delete;
    }

    @Override
    public JsonData execute(Long projectId, Long caseId) {
        LambdaQueryWrapper<ApiCaseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiCaseDO::getProjectId, projectId)
                .eq(ApiCaseDO::getId, caseId);
        ApiCaseDO apiCaseDO = apiCaseMapper.selectOne(queryWrapper);
        if(apiCaseDO != null){
            // 查找步骤
            LambdaQueryWrapper<ApiCaseStepDO> stepQueryWrapper = new LambdaQueryWrapper<>();
            stepQueryWrapper.eq(ApiCaseStepDO::getCaseId, caseId)
                    .orderByAsc(ApiCaseStepDO::getNum);
            List<ApiCaseStepDO> stepDOList = apiCaseStepMapper.selectList(stepQueryWrapper);
            if(stepDOList == null || stepDOList.isEmpty()){
                throw new BizException(BizCodeEnum.API_CASE_STEP_IS_EMPTY);
            }
            //初始化测试报告
            ReportSaveReq reportSaveReq = ReportSaveReq.builder()
                    .projectId(apiCaseDO.getProjectId())
                    .caseId(apiCaseDO.getId())
                    .type(TestTypeEnum.API.name())
                    .name(apiCaseDO.getName())
                    .executeState(ReportStateEnum.EXECUTING.name())
                    .startTime(System.currentTimeMillis())
                    .build();
            JsonData jsonData = reportFeignService.save(reportSaveReq);
            if(jsonData.isSuccess()){
                ReportDTO reportDTO = jsonData.getData( ReportDTO.class);
                CaseInfoDTO caseInfoDTO = CaseInfoDTO.builder()
                        .id(apiCaseDO.getId())
                        .moduleId(apiCaseDO.getModuleId())
                        .name(apiCaseDO.getName())
                        .build();
                System.out.println(reportDTO);
                ApiExecuteEngine apiExecuteEngine = new ApiExecuteEngine(reportDTO);
                ApiCaseResultDTO apiCaseResultDTO = apiExecuteEngine.execute(caseInfoDTO, stepDOList);
                return JsonData.buildSuccess(apiCaseResultDTO);
            }
            else{
                log.error("API interface test case execution failed, initialization test report failed,{}",apiCaseDO);
                return JsonData.buildError("API interface test case execution failed, initialization test report failed");
            }

        }else{
            return JsonData.buildError("API interface test case does not exist");
        }
    }
}
