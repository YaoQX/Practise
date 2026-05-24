package net.yao.enums;

public enum SeleniumOperationEnum {

    // 浏览器操作
    BROWSER,

    // 鼠标操作
    MOUSE,

    // 键盘操作
    KEYBOARD,

    // 等待操作
    WAIT,

    // 断言操作
    ASSERTION,

    // 针对整个浏览器页面的断言操作
    ASSERTION_BROWSER,

    // 针对页面元素的断言操作
    ASSERTION_ELEMENT,

    // 针对浏览器页面文本内容的断言操作
    ASSERTION_ELEMENT_TEXT
}
