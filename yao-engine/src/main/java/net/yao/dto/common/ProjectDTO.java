package net.yao.dto.common;


import lombok.Data;

import java.util.Date;

@Data
public class ProjectDTO {


    private Long id;

    private Long projectAdmin;

    private String name;

    private String description;

    private Date gmtCreate;

    private Date gmtModified;
}
