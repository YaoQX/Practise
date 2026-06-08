package net.yao.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AccountDTO {

    private Long id;

    private String username;

    private boolean isDisabled;

    private Date gmtCreate;

    private Date gmtModified;

    private List<RoleDTO> roleList;


}
