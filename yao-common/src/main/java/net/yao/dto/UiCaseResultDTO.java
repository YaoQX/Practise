package net.yao.dto;

import lombok.Data;

import java.util.List;

//一路整体结果
@Data
public class UiCaseResultDTO {

    private Long reportId;

    private Boolean executeState;

    private Long startTime;

    private Long endTime;

    private Long expendTime;

    private Integer quantity;

    private Integer passQuantity;

    private Integer failQuantity;

    private List<UiCaseResultItemDTO> list;

}
