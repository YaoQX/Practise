package net.yao.req.ui;

import lombok.Data;

@Data
public class UiCaseModuleSaveReq {

    private Long id;

    private Long projectId;

    private String name;
}
