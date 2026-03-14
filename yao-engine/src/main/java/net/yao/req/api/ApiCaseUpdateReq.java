package net.yao.req.api;

import lombok.Data;

@Data
public class ApiCaseUpdateReq {
    private Long id;

    private Long projectId;

    private Long moduleId;

    private String name;

    private String description;

    private String level;
}
