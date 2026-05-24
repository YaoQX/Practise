package net.yao.req.ui;

import lombok.Data;

@Data
public class UiCaseUpdateReq  {

    private Long id;

    private Long projectId;

    private Long moduleId;

    private String browser;

    private String name;

    private String description;

    private String level;


}
