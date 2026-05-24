package net.yao.service.ui.impl;

import net.yao.service.ui.SeleniumKeyboardOperationService;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

@Service
public class SeleniumKeyboardOperationServiceImpl implements SeleniumKeyboardOperationService {
    public void input(WebElement webElement, String... text){
        webElement.sendKeys(text);
    }

    public void submit(WebElement webElement){
        webElement.submit();
    }

    public void clear(WebElement webElement){
        webElement.clear();
    }
}
