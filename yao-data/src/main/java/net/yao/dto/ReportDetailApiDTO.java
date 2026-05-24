package net.yao.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReportDetailApiDTO {

    private Long id;

    private Long reportId;

    private Boolean executeState;

    private Boolean assertionState;

    private String exceptionMsg;

    private Long expendTime;

    private String requestHeader;

    private String requestQuery;

    private String requestBody;

    private String responseHeader;

    private String responseBody;

    private Long environmentId;

    private Long caseId;

    private Long num;

    private String name;

    private String description;

    private String assertion;

    private String relation;

    private String path;


    private String method;

    private String query;

    private String header;

    private String body;

    private String bodyType;

    private Date gmtCreate;

    private Date gmtModified;
}
