package net.yao.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

/**
 * 用例类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseInfoDTO {
    /**
     * 用例id/步骤id
     */
    private Long id;

    /**
     * 模块id
     */
    private Long moduleId;

    /**
     * 名称
     */
    private String name;
}
