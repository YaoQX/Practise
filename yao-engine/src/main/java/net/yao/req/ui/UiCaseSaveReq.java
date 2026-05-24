package net.yao.req.ui;


import lombok.Data;

import java.util.List;

@Data
public class UiCaseSaveReq {

    private Long projectId;

    private Long moduleId;

    private String browser;

    private String name;

    private String description;

    private String level;

    private List<UiCaseStepSaveReq> list;

}
