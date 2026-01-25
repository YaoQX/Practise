package net.yao.req.common;

import lombok.Data;

//接受请求过来的内容
@Data
public class ProjectSaveReq {

    private String name;

    private String description;

}
