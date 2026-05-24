package net.yao.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReportDetailUiDTO {
    private Long id;

    private Long reportId;

    private Boolean executeState;

    private Boolean assertionState;

    private String exceptionMsg;

    private Long expandTime;

    private Long caseId;

    private Long num;

    private String name;

    private String operationType;

    private String locationType;

    private String locationExpress;

    private Long elementWait;

    private String targetLocationType;

    private String targetLocationExpress;

    private Long targetElementWait;

    private String value;

    private String expectKey;

    private String expectValue;

    private String description;

    private Boolean isContinue;

    private Boolean isScreenshot;

    private String screenshotUrl;

    private Date gmtCreate;

    private Date gmtModified;
}
