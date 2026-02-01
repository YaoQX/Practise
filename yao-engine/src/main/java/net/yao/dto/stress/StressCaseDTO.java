package net.yao.dto.stress;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class StressCaseDTO {
    private Long id;

    private Long projectId;

    private Long moduleId;

    private Long environmentId;

    private String name;

    private String description;

    private String assertion;

    private String relation;

    private String stressSourceType;

    private String threadGroupConfig;

    private String jmxUrl;

    private String path;

    private String method;

    private String query;

    private String header;

    private String body;

    private String bodyType;

    private Date gmtCreate;

    private Date gmtModified;
}
