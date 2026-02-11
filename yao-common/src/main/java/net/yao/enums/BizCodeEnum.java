package net.yao.enums;

import lombok.Getter;

public enum BizCodeEnum {
    /**
     * ⽂件操作相关 220XXX(220模块）
     */
    FILE_REMOTE_DOWNLOAD_FAILED(220404,"Remote file download failed"),
    FILE_REMOTE_READ_FAILED(220403,"Remote file read failed"),
    FILE_REMOTE_UPLOAD_FAILED(220407,"File upload failed"),
    FILE_REMOTE_UPLOAD_IS_EMPTY(220408," The uploaded file is empty"),
    FILE_PRE_SIGNED_FAILED(220409," Temporary URL generation failed"),
    FILE_CREATE_TEMP_FAILED(220411,"Failed to generate temporary file"),
    /**
     * 压测相关
     */
    STRESS_MODULE_ID_NOT_EXIST(260001,"Module id does not exist"),
    STRESS_CASE_ID_NOT_EXIST(260002,"The test case ID does not exist"),
    STRESS_UNSUPPORTED(260005,"Unsupported pressure testing types"),
    STRESS_ASSERTION_UNSUPPORTED_ACTION(260007, "Unsupported assertion"),
    STRESS_ASSERTION_UNSUPPORTED_FROM(260008, "Unsupported source of assertion"),
    STRESS_REPORT_EXISTING(260009, "The pressure test is already running, please do not repeat it");

    @Getter
    private String message;
    @Getter
    private int code;

    private BizCodeEnum(int code, String message){
        this.code = code;
        this.message = message;
    }
}
