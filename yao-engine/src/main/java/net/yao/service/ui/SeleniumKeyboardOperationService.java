package net.yao.service.ui;

import org.openqa.selenium.WebElement;

public interface SeleniumKeyboardOperationService {

    /**
     * 键盘输入，可变参数
     */
    void input(WebElement webElement, String... text);

    /**
     * 清除输入内容
     */
    void clear(WebElement webElement);

    /**
     * 提交输入内容
     */
    void submit(WebElement webElement);
}
