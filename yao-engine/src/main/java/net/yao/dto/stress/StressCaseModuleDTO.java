package net.yao.dto.stress;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StressCaseModuleDTO {

    private Long id;

    private Long projectId;

    private String name;

    private Date gmtCreate;

    private Date gmtModified;
    //一个模块下多个用例
    List<StressCaseDTO> list;
}
