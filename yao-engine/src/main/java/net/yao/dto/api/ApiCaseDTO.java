package net.yao.dto.api;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ApiCaseDTO {

    private Long id;

    private Long projectId;

    private Long moduleId;

    private String name;

    private String description;

    private String level;

    private List<ApiCaseStepDTO> list;

    private Date gmtCreate;

    private Date gmtModified;
}
