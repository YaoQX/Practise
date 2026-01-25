package net.yao.dto.common;


import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
@Data
public class EnvironmentDTO {

    private Long id;

    private Long projectId;

    private String name;

    private String protocol;

    private String domain;

    private Integer port;

    private String description;

    private Date gmtCreate;

    private Date gmtModified;

}
