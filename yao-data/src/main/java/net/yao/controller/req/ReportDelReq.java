package net.yao.controller.req;

import lombok.Data;

@Data
public class ReportDelReq {


    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 记录id
     */
    private Long id;

    /**
     * 类型
     */
    private String type;

}
