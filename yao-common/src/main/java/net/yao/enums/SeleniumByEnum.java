package net.yao.enums;

public enum SeleniumByEnum {

    // 元素唯一标识符，html中以id属性指定
    ID,

    // 元素名称，html中以name属性指定
    NAME,

    // 元素的链接文本，位包含特定文本的链接元素
    LINK_TEXT,

    // 元素的链接文本，定位包含特定子串的链接元素
    PARTIAL_LINK_TEXT,

    // 元素的css选择器，html中以css选择器指定,以CSS规则定位元素
    CSS_SELECTOR,

    // 元素的xpath路径，html中以xpath路径指定，以xpath规则定位元素
    XPATH,

    // 元素的tagName，html中以tagName属性指定, 例如"div"、"p"等，用于定位特定标签类型的元素
    TAG_NAME,

    // 元素的className，html中以className属性指定, 例如"class1 class2"等，用于定位特定类名的元素
    CLASS_NAME
}
