package net.yao.service.ui.impl;

import net.yao.enums.BizCodeEnum;
import net.yao.enums.SeleniumByEnum;
import net.yao.exception.BizException;
import net.yao.model.UiCaseStepDO;
import net.yao.service.ui.SeleniumWaitOperationService;
import net.yao.util.SeleniumWebdriverContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class SeleniumWaitOperationServiceImpl implements SeleniumWaitOperationService {

    /**
     * 隐式等待
     * @param millSeconds 等待的毫秒数。
     */
    public void waitHide(Long millSeconds)
    {
        WebDriver webDriver = SeleniumWebdriverContext.get();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(millSeconds));
    }

    /**
     * 强制等待
     * @param millSeconds 毫秒数。
     */
    public void waitForce(Long millSeconds) {
        try {
            Thread.sleep(millSeconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void waitShow(UiCaseStepDO uiCaseStepDO) {
        WebDriverWait wait = new WebDriverWait(SeleniumWebdriverContext.get(), Duration.ofMillis(Long.parseLong(uiCaseStepDO.getValue())));
        SeleniumByEnum seleniumByEnum = SeleniumByEnum.valueOf(uiCaseStepDO.getLocationType());
        String locationExpress = uiCaseStepDO.getLocationExpress();
        switch (seleniumByEnum) {
            // 等待其出现
            case XPATH -> wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locationExpress)));
            case TAG_NAME -> wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName(locationExpress)));
            case ID -> wait.until(ExpectedConditions.presenceOfElementLocated(By.id(locationExpress)));
            case NAME -> wait.until(ExpectedConditions.presenceOfElementLocated(By.name(locationExpress)));
            case CLASS_NAME -> wait.until(ExpectedConditions.presenceOfElementLocated(By.className(locationExpress)));
            case LINK_TEXT -> wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(locationExpress)));
            case CSS_SELECTOR ->
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(locationExpress)));
            case PARTIAL_LINK_TEXT ->
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(locationExpress)));

            default -> throw new BizException(BizCodeEnum.UI_ELEMENT_NOT_EXIST);
        }
    }


}
