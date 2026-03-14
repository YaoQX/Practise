package net.yao.req.api;

import lombok.Data;

import java.util.Date;

@Data
public class ApiModuleSaveReq {


    private Long id;

    private Long projectId;

    private String name;

    private Date gmtCreate;

    private Date gmtModified;

}
