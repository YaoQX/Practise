package net.yao.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiCaseResultDTO {
    private Long reportId;

    private Boolean executeState;

    private Long startTime;

    private Long endTime;

    private Long expendTime;

    //一共几个
    private Integer quantity;

    //通过几个
    private Integer passQuantity;

    //失败几个
    private Integer failQuantity;

    //结果明细 每个步骤都算请求
    private List<ApiCaseResultItemDTO> list;
}
