package net.yao.dto.api;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ApiCaseModuleDTO {

    private Long id;

    private Long projectId;

    private String name;

    private List<ApiCaseDTO> list;

    private Date gmtCreate;

    private Date gmtModified;
}
