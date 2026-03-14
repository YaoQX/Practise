package net.yao.req.api;

import lombok.Data;


@Data
public class ApiCaseModuleUpdateReq {

    private Long id;

    private Long projectId;

    private String name;

}
