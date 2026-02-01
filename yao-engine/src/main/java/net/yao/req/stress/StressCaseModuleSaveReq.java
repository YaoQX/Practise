package net.yao.req.stress;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class StressCaseModuleSaveReq {
    private Long id;

    private Long projectId;

    private String name;
}
