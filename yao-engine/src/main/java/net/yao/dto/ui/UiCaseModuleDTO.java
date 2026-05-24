package net.yao.dto.ui;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UiCaseModuleDTO {

    private Long id;

    private Long projectId;

    private String name;

    private Date gmtCreate;

    private Date gmtModified;

    private List<UiCaseDTO> list;
}
