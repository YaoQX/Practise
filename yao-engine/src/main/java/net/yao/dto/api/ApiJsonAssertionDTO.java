package net.yao.dto.api;

import lombok.Data;

@Data
public class ApiJsonAssertionDTO {

    /**
     * 取值来源， header,body
     */
    private String from;

    /**
     * 类型
     */
    private String type;

    /**
     * 断言方式
     */
    private String action;

    /**
     * 表达式
     */
    private String express;

    /**
     * 预期值
     */
    private String value;
}

