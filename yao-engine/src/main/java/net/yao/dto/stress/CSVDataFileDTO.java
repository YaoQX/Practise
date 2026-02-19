package net.yao.dto.stress;

import lombok.Data;

@Data
public class CSVDataFileDTO {
    /**
     * 文件名
     */
    private String name;

    /**
     * 远程地址
     */
    private String remoteFilePath;

    /**
     * 本地地址
     */
    private String localFilePath;

    /**
     * 类型 CSV
     */
    private String sourceType;

    /**
     * 分隔符
     */
    private String delimiter;

    /**
     * 无视首行
     */
    private Boolean ignoreFirstLine = false;

    /**
     * 是否循环读取
     */
    private Boolean recycle = true;

    /**
     * 变ᰁ名
     */
    private String variableNames;
}
