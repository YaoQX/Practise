package net.yao.dto.api;

import lombok.Data;

import java.util.Date;

@Data
public class ApiCaseDTO {

    private Long id;

    private Long projectId;

    private Long moduleId;

    private String name;

    private String description;

    private String level;

    private Date gmtCreate;

    private Date gmtModified;
}
