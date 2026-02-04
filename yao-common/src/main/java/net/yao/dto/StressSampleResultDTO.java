package net.yao.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class StressSampleResultDTO {

    private Long id;

    private Long reportId;

    private String assertInfo;

    private Long errorCount;

    private Double errorPercentage;

    private Integer maxTime;

    private Double meanTime;

    private Integer minTime;

    private Double receiveKBPerSecond;

    private Double sentKBPerSecond;

    private String requestLocation;

    private String requestHeader;

    private String requestBody;

    private Double requestRate;

    private String responseCode;

    private String responseData;

    private String responseHeader;

    private Long samplerCount;

    private String samplerLabel;

    private Long sampleTime;

    private Date gmtCreate;

    private Date gmtModified;
}
