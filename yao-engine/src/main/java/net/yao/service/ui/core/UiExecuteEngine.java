package net.yao.service.ui.core;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.yao.dto.*;
import net.yao.dto.common.CaseInfoDTO;
import net.yao.enums.TestTypeEnum;
import net.yao.model.UiCaseStepDO;
import net.yao.service.common.FileService;
import net.yao.service.common.ResultSenderService;
import net.yao.service.ui.SeleniumDispatcherService;
import net.yao.util.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.mock.web.MockMultipartFile;

@Data
@Slf4j
public class UiExecuteEngine {

    private ReportDTO reportDTO;

    private ResultSenderService resultSenderService;

    private SeleniumDispatcherService seleniumDispatcherService;

    private FileService fileService;

    public UiExecuteEngine(ReportDTO reportDTO){
        this.reportDTO = reportDTO;
        resultSenderService =  (ResultSenderService) SpringContextHolder.getBean("kafkaSenderServiceImpl");
//        resultSenderService = SpringContextHolder.getBean(ResultSenderService.class);
        seleniumDispatcherService = SpringContextHolder.getBean(SeleniumDispatcherService.class);
        fileService = SpringContextHolder.getBean(FileService.class);
    }

    /**
     * 在测试失败或需要存证的时候，让浏览器把当前的网页画面截个图，
     * 然后自动上传到的服务器或云存储，最后吐出一个能直接在浏览器里打开的图片网址（URL）。
     */
    private String getScreenshot(){
        WebDriver driver = SeleniumWebdriverContext.get();
        //命令浏览器立刻对当前的网页进行全屏截图
        File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try
        {
            MockMultipartFile multipartFile =  new MockMultipartFile(file.getName(),file.getName(), Files.probeContentType(file.toPath()),new FileInputStream(file));
            return fileService.upload(multipartFile);
        }catch (Exception e){
            log.error("Screenshot Error",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 递归方法 一连串命令
     */
    private UiCaseResultDTO doExecute(UiCaseResultDTO result, List<UiCaseStepDO> stepList)
    {
        if(result == null)
        {
            result = new UiCaseResultDTO();
            result.setList(new ArrayList<>());
        }
        //递归到这个为空的时候，就返回结果
        if (stepList == null || stepList.isEmpty()){
            return result;
        }
        UiCaseStepDO uiCaseStepDO = stepList.get(0);

        UiCaseResultItemDTO resultItemDTO = new UiCaseResultItemDTO();
        result.getList().add(resultItemDTO);

        UiCaseStepDTO uiCaseStepDTO = SpringBeanUtil.copyProperties(uiCaseStepDO, UiCaseStepDTO.class);

        resultItemDTO.setUiCaseStep(uiCaseStepDTO);
        resultItemDTO.setAssertionState(true);
        resultItemDTO.setExecuteState(true);

        try
        {
            long startTime = System.currentTimeMillis();

            UiOperationResultDTO operationResultDTO = seleniumDispatcherService.operationDispatcher(uiCaseStepDO);
            operationResultDTO.setOperationType(uiCaseStepDO.getOperationType());
            long endTime = System.currentTimeMillis();

            //如果步骤要截图
            if(uiCaseStepDO.getIsScreenshot()){
                resultItemDTO.setScreenshotUrl(getScreenshot());
            }

            //配置当前步骤结束信息
            resultItemDTO.setAssertionState(operationResultDTO.getOperationState());
            if(operationResultDTO.getOperationState()){
                resultItemDTO.setExceptionMsg("Action:"+operationResultDTO.getOperationType()+",Actual content："+operationResultDTO.getActualValue()+",Expected content："+operationResultDTO.getActualValue());
            }

            resultItemDTO.setExpendTime(endTime-startTime);
            if(!operationResultDTO.getOperationState() && !uiCaseStepDO.getIsContinue()){
                //操作失败后且不再继续
                return result;
            }

        } catch (Exception e) {
            resultItemDTO.setExecuteState(false);
            resultItemDTO.setAssertionState(false);

            //记录异常
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            resultItemDTO.setExceptionMsg(sw.toString());

            //步骤需要截图
            if(uiCaseStepDO.getIsScreenshot()){
                resultItemDTO.setScreenshotUrl(getScreenshot());
            }
            if(!uiCaseStepDO.getIsContinue()){
                return result;
            }
        }
        stepList.remove(0);
        return doExecute(result,stepList);

    }

    public UiCaseResultDTO execute(CaseInfoDTO caseInfoDTO, String browser, List<UiCaseStepDO> stepList)
    {
        WebDriver webDriver = SeleniumFetchUtil.getDriver( browser);
        SeleniumWebdriverContext.set(webDriver);
        try
        {
            int quantity = stepList.size();

            long startTime = System.currentTimeMillis();
            UiCaseResultDTO result = doExecute(null, stepList);
            long endTime = System.currentTimeMillis();

            result.setReportId(reportDTO.getId());
            result.setStartTime(startTime);
            result.setEndTime(endTime);
            result.setExpendTime(endTime-startTime);
            result.setQuantity(quantity);

            // 去刚才跑完的那一堆步骤结果里，精确统计出到底有多少个步骤是真正完全成功的。
            int passQuantity = result.getList().stream().filter(item -> {

                item.setReportId(reportDTO.getId());

                return item.getExecuteState() && item.getAssertionState();

            }).toList().size();

            result.setPassQuantity(passQuantity);
            result.setFailQuantity(quantity-passQuantity);
            result.setExecuteState(Objects.equals(result.getQuantity(),result.getPassQuantity()));

            //发送测试报告
            resultSenderService.sendResult(caseInfoDTO, TestTypeEnum.UI, JsonUtil.obj2Json(result));

            return result;
        }finally {
            try {
                if(webDriver!=null){
                    //方便本地测试查看，临时加个慢退出 TODO
                    TimeUnit.SECONDS.sleep(5);
                    webDriver.quit();
                }
            }catch (Exception e){
                log.error("Failed to close browser driver",e);
            }
            // 清理上下文
            SeleniumWebdriverContext.remove();

        }
    }

}
