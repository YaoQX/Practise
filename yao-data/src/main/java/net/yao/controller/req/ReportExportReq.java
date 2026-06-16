package net.yao.controller.req;

import lombok.Data;

@Data
public class ReportExportReq {

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 用例id
     */
    private Long caseId;

    /**
     * 用例类型，API, UI, STRESS
     */
    private String type;

    /**
     * 用例名称
     */
    private String name;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

}
