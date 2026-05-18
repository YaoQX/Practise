package net.yao.util;

public class ApiRegexpUtil {

    /**
     * 匹配{{}}中间的东西
     */
    public static final String REGEXP = "\\{\\{([^}]+)}}";

    /**
     * 根据{{}}里名字匹配 只匹配这个名字
     */
    public static String byName(String name) {
        return "\\{\\{("+ name +")}}";
    }
}
