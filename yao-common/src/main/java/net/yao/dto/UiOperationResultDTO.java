package net.yao.dto;

import lombok.Builder;
import lombok.Data;

//这个步骤实际执行后的结果
@Data
@Builder
public class UiOperationResultDTO {
    /**
     * 操作的状态
     */
    private Boolean operationState;

    /**
     * 操作类型
     */
    private String operationType;


    /**
     * 期望内容
     */
    private Object expectValue;

    /**
     * 实际内容
     */
    private Object actualValue;
}
