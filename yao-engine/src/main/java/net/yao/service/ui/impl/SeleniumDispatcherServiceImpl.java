package net.yao.service.ui.impl;

import lombok.extern.slf4j.Slf4j;
import net.yao.dto.UiOperationResultDTO;
import net.yao.enums.BizCodeEnum;
import net.yao.enums.SeleniumOperationEnum;
import net.yao.enums.SeleniumOperationTypeEnum;
import net.yao.exception.BizException;
import net.yao.model.UiCaseStepDO;
import net.yao.service.ui.*;
import net.yao.util.SeleniumWebdriverContext;
import net.yao.util.SeleniumFetchUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static net.yao.enums.SeleniumOperationTypeEnum.*;

@Slf4j
@Service
public class SeleniumDispatcherServiceImpl implements SeleniumDispatcherService {

    @Autowired
    private SeleniumBrowserOperationService seleniumBrowserOperationService;

    @Autowired
    private SeleniumWaitOperationService seleniumWaitOperationService;

    @Autowired
    private SeleniumMouseOperationService seleniumMouseOperationService;

    @Autowired
    private SeleniumAssertionOperationService seleniumAssertionOperationService;

    @Autowired
    private SeleniumKeyboardOperationService seleniumKeyboardOperationService;

    public UiOperationResultDTO operationDispatcher(UiCaseStepDO uiCaseStepDO) {

        String type = uiCaseStepDO.getOperationType();
        if(type.startsWith(SeleniumOperationEnum.BROWSER.name()))
        {
             browserOperationDispatcher(uiCaseStepDO);
        }
        else if(type.startsWith(SeleniumOperationEnum.MOUSE.name()))
        {
            mouseOperationDispatcher(uiCaseStepDO);
        }
        else if(type.startsWith(SeleniumOperationEnum.KEYBOARD.name()))
        {
            keyboardOperationDispatcher(uiCaseStepDO);
        }
        else if(type.startsWith(SeleniumOperationEnum.WAIT.name()))
        {
             waitOperationDispatcher(uiCaseStepDO);
        }
        else if(type.startsWith(SeleniumOperationEnum.ASSERTION.name()))
        {
            return assertionOperationDispatcher(uiCaseStepDO);
        }
        else
        {
            log.error("Unsupported UI Operation Type：{}", type);
            throw new BizException(BizCodeEnum.UI_OPERATION_UNSUPPORTED);
        }
        return UiOperationResultDTO.builder().operationState(true).build();
    }

    private UiOperationResultDTO assertionOperationDispatcher(UiCaseStepDO uiCaseStepDO) {

        String type = uiCaseStepDO.getOperationType();
        UiOperationResultDTO operationResult = null;
        if (type.contains(SeleniumOperationEnum.ASSERTION_BROWSER.name())) {
            //断言浏览器相关操作
            operationResult = assertionBrowserOperationDispatcher(uiCaseStepDO);

        } else if (type.contains(SeleniumOperationEnum.ASSERTION_ELEMENT_TEXT.name())) {
            //断言元素文本相关操作
            operationResult = assertionElementTextOperationDispatcher(uiCaseStepDO);

        } else if (type.contains(SeleniumOperationEnum.ASSERTION_ELEMENT.name())) {
            //断言元素属性状态相关操作
            operationResult = assertionElementOperationDispatcher(uiCaseStepDO);

        } else {
            throw new BizException(BizCodeEnum.UI_OPERATION_UNSUPPORTED_ASSERTION);
        }

        return operationResult;
    }

    // 验证页面元素的各种状态
    private UiOperationResultDTO assertionElementOperationDispatcher(UiCaseStepDO uiCaseStepDO) {

        SeleniumOperationTypeEnum operationTypeEnum = SeleniumOperationTypeEnum.valueOf(uiCaseStepDO.getOperationType());
        return switch (operationTypeEnum) {
            case ASSERTION_ELEMENT_EXIST -> seleniumAssertionOperationService.existElement(uiCaseStepDO.getLocationType(), uiCaseStepDO.getLocationExpress());
            case ASSERTION_ELEMENT_NOT_EXIST -> seleniumAssertionOperationService.absentElement(uiCaseStepDO.getLocationType(), uiCaseStepDO.getLocationExpress());
            case ASSERTION_ELEMENT_ENABLE -> seleniumAssertionOperationService.enableElement(uiCaseStepDO.getLocationType(), uiCaseStepDO.getLocationExpress());
            case ASSERTION_ELEMENT_DISABLE -> seleniumAssertionOperationService.disableElement(uiCaseStepDO.getLocationType(), uiCaseStepDO.getLocationExpress());
            case ASSERTION_ELEMENT_VISIBLE -> seleniumAssertionOperationService.visibleElement(uiCaseStepDO.getLocationType(), uiCaseStepDO.getLocationExpress());
            case ASSERTION_ELEMENT_INVISIBLE -> seleniumAssertionOperationService.invisibleElement(uiCaseStepDO.getLocationType(), uiCaseStepDO.getLocationExpress());
            case ASSERTION_ELEMENT_SELECT -> seleniumAssertionOperationService.selectElement(uiCaseStepDO.getLocationType(), uiCaseStepDO.getLocationExpress());
            case ASSERTION_ELEMENT_UNSELECT -> seleniumAssertionOperationService.unselectElement(uiCaseStepDO.getLocationType(), uiCaseStepDO.getLocationExpress());
            default -> {
                log.error("Unsuppored UI Operation Type：{}", uiCaseStepDO.getOperationType());
                throw new BizException(BizCodeEnum.UI_OPERATION_UNSUPPORTED_ASSERTION);
            }
        };

    }

    private void browserOperationDispatcher(UiCaseStepDO uiCaseStepDO) {
        String type = uiCaseStepDO.getOperationType();
        SeleniumOperationTypeEnum operationType = SeleniumOperationTypeEnum.valueOf(type);
        switch (operationType) {
            case BROWSER_OPEN -> seleniumBrowserOperationService.open(uiCaseStepDO.getValue());
            case BROWSER_CLOSE -> seleniumBrowserOperationService.close();
            case BROWSER_BACK -> seleniumBrowserOperationService.back();
            case BROWSER_FORWARD -> seleniumBrowserOperationService.forward();
            case BROWSER_REFRESH -> seleniumBrowserOperationService.refresh();
            case BROWSER_MAXIMIZE -> seleniumBrowserOperationService.resizeMax();
            case BROWSER_RESIZE -> {
                String[] split = uiCaseStepDO.getValue().split(",");
                seleniumBrowserOperationService.resize(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            }
            default ->{
                log.error("Unsupported Browser Operation Type：{}", operationType);
                throw new BizException(BizCodeEnum.UI_OPERATION_UNSUPPORTED_BROWSER);
            }
        }
    }

    private void mouseOperationDispatcher(UiCaseStepDO uiCaseStepDO) {

        // 查找UI元素
        WebElement webElement = SeleniumFetchUtil.findElement(uiCaseStepDO.getLocationType(), uiCaseStepDO.getLocationExpress(), uiCaseStepDO.getElementWait());

        SeleniumOperationTypeEnum operationTypeEnum = SeleniumOperationTypeEnum.valueOf(uiCaseStepDO.getOperationType());
        switch (operationTypeEnum) {
            case MOUSE_LEFT_CLICK -> seleniumMouseOperationService.leftClick(webElement);
            case MOUSE_RIGHT_CLICK -> seleniumMouseOperationService.rightClick(webElement);
            case MOUSE_DOUBLE_CLICK -> seleniumMouseOperationService.doubleClick(webElement);
            case MOUSE_MOVE_TO_ELEMENT -> seleniumMouseOperationService.moveToElement(webElement);
            case MOUSE_DRAG_ELEMENT_TO_ELEMENT -> seleniumMouseOperationService.dragAndDrop(webElement, SeleniumFetchUtil.findElement(uiCaseStepDO.getTargetLocationType(), uiCaseStepDO.getTargetLocationExpress(), uiCaseStepDO.getTargetElementWait()));
            default -> {
                log.error("Unsupported Mouse Operation Type：{}", operationTypeEnum);
                throw new BizException(BizCodeEnum.UI_OPERATION_UNSUPPORTED_MOUSE);
            }
        }

    }

    private void keyboardOperationDispatcher(UiCaseStepDO uiCaseStepDO) {
        WebElement webElement = SeleniumFetchUtil.findElement(uiCaseStepDO.getLocationType(), uiCaseStepDO.getLocationExpress(), uiCaseStepDO.getElementWait());

        SeleniumOperationTypeEnum operationTypeEnum = SeleniumOperationTypeEnum.valueOf(uiCaseStepDO.getOperationType());

        switch (operationTypeEnum) {
            case KEYBOARD_INPUT -> seleniumKeyboardOperationService.input(webElement,  uiCaseStepDO.getValue());
            case KEYBOARD_SUBMIT -> seleniumKeyboardOperationService.submit(webElement);
            case KEYBOARD_CLEAR -> seleniumKeyboardOperationService.clear(webElement);
            default -> {
                log.error("Unsupported Keyboard Operation Type：{}", operationTypeEnum);
                throw new BizException(BizCodeEnum.UI_OPERATION_UNSUPPORTED_KEYBOARD);
            }
        }
    }

    private void waitOperationDispatcher(UiCaseStepDO uiCaseStepDO) {
           SeleniumOperationTypeEnum operationTypeEnum = SeleniumOperationTypeEnum.valueOf(uiCaseStepDO.getOperationType());
           switch (operationTypeEnum) {
               case WAIT_HIDE -> seleniumWaitOperationService.waitHide(Long.parseLong(uiCaseStepDO.getValue()));
               case WAIT_SHOW -> seleniumWaitOperationService.waitShow(uiCaseStepDO);
               case WAIT_FORCE -> seleniumWaitOperationService.waitForce(Long.parseLong(uiCaseStepDO.getValue()));
               default -> {
                   log.error("Unsupported Wait Operation Type：{}", operationTypeEnum);
                   throw new BizException(BizCodeEnum.UI_OPERATION_UNSUPPORTED_WAIT);
               }
           }
    }

    // 浏览器全局断言
    private UiOperationResultDTO assertionBrowserOperationDispatcher(UiCaseStepDO uiCaseStepDO) {
        WebDriver webDriver = SeleniumWebdriverContext.get();
        SeleniumOperationTypeEnum operationTypeEnum = SeleniumOperationTypeEnum.valueOf(uiCaseStepDO.getOperationType());
        return switch (operationTypeEnum) {
            case ASSERTION_BROWSER_TITLE_EQUAL -> seleniumAssertionOperationService.equalValue(webDriver.getTitle(), uiCaseStepDO.getExpectValue());
            case ASSERTION_BROWSER_TITLE_NOT_EQUAL ->
                    seleniumAssertionOperationService.notEqualValue(webDriver.getTitle(), uiCaseStepDO.getExpectValue());
            case ASSERTION_BROWSER_TITLE_CONTAIN ->
                    seleniumAssertionOperationService.containValue(webDriver.getTitle(), uiCaseStepDO.getExpectValue());
            case ASSERTION_BROWSER_TITLE_NOT_CONTAIN ->
                    seleniumAssertionOperationService.notContainValue(webDriver.getTitle(), uiCaseStepDO.getExpectValue());
            case ASSERTION_BROWSER_URL_EQUAL ->
                    seleniumAssertionOperationService.equalValue(webDriver.getCurrentUrl(), uiCaseStepDO.getExpectValue());
            case ASSERTION_BROWSER_URL_NOT_EQUAL ->
                    seleniumAssertionOperationService.notEqualValue(webDriver.getCurrentUrl(), uiCaseStepDO.getExpectValue());
            case ASSERTION_BROWSER_URL_CONTAIN ->
                    seleniumAssertionOperationService.containValue(webDriver.getCurrentUrl(), uiCaseStepDO.getExpectValue());
            case ASSERTION_BROWSER_URL_NOT_CONTAIN ->
                    seleniumAssertionOperationService.notContainValue(webDriver.getCurrentUrl(), uiCaseStepDO.getExpectValue());
            default -> {
                log.error("Unsupported Assertion Type：{}", uiCaseStepDO.getOperationType());
                throw new BizException(BizCodeEnum.UI_OPERATION_UNSUPPORTED_ASSERTION);
            }
        };

    }

    // 元素文本断言
    private UiOperationResultDTO assertionElementTextOperationDispatcher(UiCaseStepDO uiCaseStepDO) {
        WebElement element = null;
        try {
            element = SeleniumFetchUtil.findElement(uiCaseStepDO.getLocationType(), uiCaseStepDO.getLocationExpress(), uiCaseStepDO.getElementWait());
        }catch (Exception e){
            //元素不存在
            throw new BizException(BizCodeEnum.UI_ELEMENT_NOT_EXIST);
        }

        String value = element.getText();
        SeleniumOperationTypeEnum operationTypeEnum = SeleniumOperationTypeEnum.valueOf(uiCaseStepDO.getOperationType());

        return switch (operationTypeEnum) {
            case ASSERTION_ELEMENT_TEXT_GREAT_THEN ->
                    seleniumAssertionOperationService.greaterThan(Long.parseLong(value), Long.parseLong(uiCaseStepDO.getExpectValue()));
            case ASSERTION_ELEMENT_TEXT_LESS_THEN ->
                    seleniumAssertionOperationService.lessThan(Long.parseLong(value), Long.parseLong(uiCaseStepDO.getExpectValue()));
            case ASSERTION_ELEMENT_TEXT_EQUAL ->
                    seleniumAssertionOperationService.equalValue(value, uiCaseStepDO.getExpectValue());
            case ASSERTION_ELEMENT_TEXT_NOT_EQUAL ->
                    seleniumAssertionOperationService.notEqualValue(value, uiCaseStepDO.getExpectValue());
            case ASSERTION_ELEMENT_TEXT_CONTAIN ->
                    seleniumAssertionOperationService.containValue(value, uiCaseStepDO.getExpectValue());
            case ASSERTION_ELEMENT_TEXT_NOT_CONTAIN ->
                    seleniumAssertionOperationService.notContainValue(value, uiCaseStepDO.getExpectValue());
            default -> {
                log.error("Unsupported Assertion Type：{}", uiCaseStepDO.getOperationType());
                throw new BizException(BizCodeEnum.UI_OPERATION_UNSUPPORTED_ASSERTION);
            }
        };
    }



}
