package net.yao.req;

import lombok.Data;

@Data
public class ReportDelReq {


    /**
     * 项目id
     */
    private Long projectId;

    /**
     * id
     */
    private Long id;

    /**
     * 类型
     */
    private String type;

}
