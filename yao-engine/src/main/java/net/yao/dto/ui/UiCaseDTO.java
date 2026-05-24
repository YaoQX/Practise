package net.yao.dto.ui;

import lombok.Data;
import net.yao.dto.UiCaseStepDTO;

import java.util.Date;
import java.util.List;

@Data
public class UiCaseDTO {

    private Long id;

    private Long projectId;

    private Long moduleId;

    private String browser;

    private String description;

    private String level;

    private Date gmtCreate;

    private Date gmtModified;

    private List<UiCaseStepDTO> list;
}
