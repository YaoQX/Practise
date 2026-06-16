package net.yao.util;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.data.ReadCellData;

import com.alibaba.excel.metadata.property.ExcelContentProperty;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    public static <T> void exportExcel(HttpServletResponse response, List<T> dataList, String fileName) {
        try {
            if (fileName == null || fileName.trim().isEmpty()) {
                fileName = "report_export";
            }

            // 不这样文件名就乱码了
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader(
                    "Content-Disposition",
                    "attachment;filename*=utf-8''" + encodedFileName + ".xlsx"
            );

            if(dataList!=null && !dataList.isEmpty()){
                EasyExcel.write(response.getOutputStream()).excelType(ExcelTypeEnum.XLSX)
                        .head(dataList.get(0).getClass()).sheet("sheet1").doWrite(dataList);
            }else {
                EasyExcel.write(response.getOutputStream()).excelType(ExcelTypeEnum.XLSX)
                        .head(Collections.emptyList()).sheet("sheet1").doWrite(dataList);
            }

        } catch (Exception e) {
            try {
                response.reset();
                response.setContentType("application/json;charset=utf-8");
                response.setCharacterEncoding("utf-8");

                Map<String, Object> result = new HashMap<>();
                result.put("code", -1);
                result.put("success", false);
                result.put("msg", "Export Excel failed: " + e.getMessage());

                String json = new ObjectMapper().writeValueAsString(result);
                response.getWriter().write(json);

            } catch (IOException ioException) {
                throw new RuntimeException("Export Excel failed, and write error response failed", ioException);
            }
        }


    }
    /**
     * Long 时间戳转换器
     *
     * 默认认为 Long 是毫秒时间戳：
     * 例如 System.currentTimeMillis()
     *
     * Excel 显示格式：
     * yyyy-MM-dd HH:mm:ss
     */
    public static class LongTimeConverter implements Converter<Long> {

        private static final DateTimeFormatter FORMATTER =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @Override
        public Class<?> supportJavaTypeKey() {
            return Long.class;
        }

        @Override
        public CellDataTypeEnum supportExcelTypeKey() {
            return CellDataTypeEnum.STRING;
        }

        /**
         * Java -> Excel
         */
        @Override
        public WriteCellData<String> convertToExcelData(Long value,
                                                        ExcelContentProperty contentProperty,
                                                        GlobalConfiguration globalConfiguration) {
            if (value == null) {
                return new WriteCellData<>("");
            }

            LocalDateTime dateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(value),
                    ZoneId.systemDefault()
            );

            return new WriteCellData<>(FORMATTER.format(dateTime));
        }

        /**
         * Excel -> Java
         *
         */
        @Override
        public Long convertToJavaData(ReadCellData<?> cellData,
                                      ExcelContentProperty contentProperty,
                                      GlobalConfiguration globalConfiguration) {
            String value = cellData.getStringValue();

            if (value == null || value.trim().isEmpty()) {
                return null;
            }

            LocalDateTime dateTime = LocalDateTime.parse(value, FORMATTER);

            return dateTime.atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli();
        }
    }


}

