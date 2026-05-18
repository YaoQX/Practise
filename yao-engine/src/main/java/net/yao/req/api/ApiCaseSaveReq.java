package net.yao.req.api;

import lombok.Data;

import java.util.List;

@Data
public class ApiCaseSaveReq {
    private Long id;

    private Long projectId;

    private Long moduleId;

    private String name;

    private String description;

    private String level;

    private List<ApiCaseStepSaveReq> list;
}
