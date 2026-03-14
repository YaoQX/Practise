package net.yao.req.api;

import lombok.Data;


@Data
public class ApiCaseModuleSaveReq  {

    private Long id;

    private Long projectId;

    private String name;

}
