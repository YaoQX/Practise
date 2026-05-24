package net.yao.dto;

import lombok.Data;

//某一个用例步骤的执行结果
@Data
public class UiCaseResultItemDTO {
    /**
     * 报告ID
     */
    private Long reportId;

    /**
     * 执行状态
     */
    private Boolean executeState;

    /**
     * 断言状态
     */
    private Boolean assertionState;

    /**
     * 异常信息
     */
    private String exceptionMsg;

    /**
     * 耗时
     */
    private Long expendTime;

    /**
     * 截图地址
     */
    private String screenshotUrl;

    /**
     * 用例步骤
     */
    private UiCaseStepDTO uiCaseStep;
}
