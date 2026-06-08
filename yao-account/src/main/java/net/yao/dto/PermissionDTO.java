package net.yao.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PermissionDTO {

    private Long id;

    private String name;

    private String code;

    private String description;

    private Date gmtCreate;

    private Date gmtModified;
}
