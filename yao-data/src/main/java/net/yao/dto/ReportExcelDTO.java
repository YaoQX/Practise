package net.yao.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import net.yao.util.ExcelUtil;

import java.util.Date;

@Data
public class ReportExcelDTO {

    @ExcelProperty("ID")
    @ColumnWidth(10)
    private Long id;

    @ExcelProperty("Project ID")
    @ColumnWidth(12)
    private Long projectId;

    @ExcelProperty("Case ID")
    @ColumnWidth(12)
    private Long caseId;

    @ExcelProperty("ReportType")
    @ColumnWidth(15)
    private String type;

    @ExcelProperty("ReportName")
    @ColumnWidth(25)
    private String name;

    @ExcelProperty("Execution Status")
    @ColumnWidth(20)
    private String executeState;

    @ExcelProperty(value = "Start Time", converter = ExcelUtil.LongTimeConverter.class)
    @ColumnWidth(20)
    private Long startTime;

    @ExcelProperty(value = "End Time", converter = ExcelUtil.LongTimeConverter.class)
    @ColumnWidth(20)
    private Long endTime;

    @ExcelProperty("expend Time")
    @ColumnWidth(15)
    private Long expendTime;

    @ExcelProperty("quantity")
    @ColumnWidth(15)
    private Long quantity;

    @ExcelProperty("Pass Quantity")
    @ColumnWidth(15)
    private Long passQuantity;

    @ExcelProperty("Fail Quantity")
    @ColumnWidth(15)
    private Long failQuantity;

    @ExcelProperty("Report Summary")
    @ColumnWidth(40)
    private String summary;

    @ExcelProperty("Created Time")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(25)
    private Date gmtCreate;

    @ExcelProperty("Modified Time")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(25)
    private Date gmtModified;
}