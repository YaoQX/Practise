package net.yao.req.api;

import lombok.Data;

@Data
public class ApiUpdateReq {
    private Long id;

    private Long projectId;

    private Long moduleId;

    private Long environmentId;

    private String name;

    private String description;


    private String level;


    private String path;


    private String method;

    private String query;


    private String header;


    private String body;

    private String bodyType;
}
