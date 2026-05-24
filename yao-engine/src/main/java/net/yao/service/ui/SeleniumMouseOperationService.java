package net.yao.service.ui;

import org.openqa.selenium.WebElement;

public interface SeleniumMouseOperationService {

    /**
     * 左键单击
     * @param webElement 要单击的网页元素
     */
    void leftClick(WebElement webElement);

    /**
     * 右键单击
     */
    void rightClick(WebElement webElement);

    /**
     * 双击
     */
    void doubleClick(WebElement webElement);

    /**
     * 把源网页元素拖到目标元素位置
     * @param sourceElement 源元素
     * @param targetElement 目标元素
     */
    void dragAndDrop(WebElement sourceElement, WebElement targetElement);

    /**
     * 鼠标移动到元素
     * @param webElement 要移动到的元素
     */
    void moveToElement(WebElement webElement);
}
