package net.yao.service.ui.impl;

import net.yao.service.ui.SeleniumBrowserOperationService;
import net.yao.util.SeleniumWebdriverContext;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SeleniumBrowserOperationServiceImpl implements SeleniumBrowserOperationService {

    public void open(String url) {
        WebDriver webDriver = SeleniumWebdriverContext.get();
        webDriver.get(url);
    }

    public void close() {
        WebDriver webDriver = SeleniumWebdriverContext.get();
        webDriver.quit();
    }

    public void back() {
        WebDriver webDriver = SeleniumWebdriverContext.get();
        webDriver.navigate().back();
    }

    public void forward() {
        WebDriver webDriver = SeleniumWebdriverContext.get();
        webDriver.navigate().forward();
    }

    public void refresh() {
        WebDriver webDriver = SeleniumWebdriverContext.get();
        webDriver.navigate().refresh();
    }

    public void resizeMax() {
        WebDriver webDriver = SeleniumWebdriverContext.get();
        webDriver.manage().window().maximize();
    }

    public void resize(int width, int height) {
        WebDriver webDriver = SeleniumWebdriverContext.get();
        webDriver.manage().window().setSize(new org.openqa.selenium.Dimension(width, height));
    }

    public void switchByHandle(String handler) {
        WebDriver webDriver = SeleniumWebdriverContext.get();
        webDriver.switchTo().window(handler);
    }

    public void switchByIndex(int index) {
        WebDriver webDriver = SeleniumWebdriverContext.get();
        Set<String> windowHandles = webDriver.getWindowHandles();
        String[] array = windowHandles.toArray(new String[]{});
        webDriver.switchTo().window(array[index]);
    }



}
