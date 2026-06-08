package net.yao.dto;

import lombok.Data;
import net.yao.model.PermissionDO;

import java.util.Date;
import java.util.List;

@Data
public class RoleDTO {

    private Long id;

    private String name;

    private String description;

    private Date gmtCreate;

    private Date gmtModified;

    private String code;

    private List<PermissionDTO> permissionList;
}
