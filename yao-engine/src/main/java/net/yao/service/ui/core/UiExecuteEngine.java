package net.yao.service.ui.core;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.yao.dto.ReportDTO;
import net.yao.dto.UiCaseResultDTO;
import net.yao.model.UiCaseStepDO;
import net.yao.service.common.FileService;
import net.yao.service.common.ResultSenderService;
import net.yao.service.ui.SeleniumDispatcherService;
import net.yao.util.SeleniumWebdriverContext;
import net.yao.util.SpringContextHolder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.List;

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
        resultSenderService = SpringContextHolder.getBean(ResultSenderService.class);
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
     * 递归方法
     */
    private UiCaseResultDTO doExecute(UiCaseResultDTO result, List<UiCaseStepDO> stepList)
    {

    }

}
