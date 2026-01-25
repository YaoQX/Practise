package net.yao.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author yaoqx,
 * @since 2026-01-18
 */
@Getter
@Setter
@TableName("environment")
@Schema(name = "EnvironmentDO", description = "")
public class EnvironmentDO implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "所属项⽬ID")
    @TableField("project_id")
    private Long projectId;

    @Schema(description = "环境名称")
    @TableField("name")
    private String name;

    @Schema(description = "协议")
    @TableField("protocol")
    private String protocol;

    @Schema(description = "环境域名")
    @TableField("domain")
    private String domain;

    @Schema(description = "端⼝")
    @TableField("port")
    private Integer port;

    @Schema(description = "描述")
    @TableField("description")
    private String description;

    @Schema(description = "创建时间")
    @TableField("gmt_create")
    private Date gmtCreate;

    @Schema(description = "更新时间")
    @TableField("gmt_modified")
    private Date gmtModified;
}
