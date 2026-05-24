package net.yao.service.ui.impl;

import net.yao.service.ui.SeleniumMouseOperationService;
import net.yao.util.SeleniumWebdriverContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;

@Service
public class SeleniumMouseOperationServiceImpl implements SeleniumMouseOperationService {

    public void leftClick(WebElement webElement) {
             webElement.click();
    }

    public void rightClick(WebElement webElement) {
        Actions actions = new Actions(SeleniumWebdriverContext.get());
        actions.contextClick(webElement).perform();
    }

    public void doubleClick(WebElement webElement) {
        Actions actions = new Actions(SeleniumWebdriverContext.get());
        actions.doubleClick(webElement).perform();
    }

    public void dragAndDrop(WebElement sourceElement, WebElement targetElement) {
        Actions actions = new Actions(SeleniumWebdriverContext.get());
        actions.dragAndDrop(sourceElement, targetElement).perform();
    }

    public void moveToElement(WebElement webElement) {
        Actions actions = new Actions(SeleniumWebdriverContext.get());
        actions.moveToElement(webElement).perform();
    }


}
