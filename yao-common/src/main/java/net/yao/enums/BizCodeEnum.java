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
    STRESS_ENVIRONMENT_ID_NOT_EXIST(260001,"Environment id does not exist"),
    STRESS_CASE_ID_NOT_EXIST(260002,"The test case ID does not exist"),
    STRESS_UNSUPPORTED(260005,"Unsupported pressure testing types"),
    STRESS_ASSERTION_UNSUPPORTED_ACTION(260007, "Unsupported assertion"),
    STRESS_ASSERTION_UNSUPPORTED_FROM(260008, "Unsupported source of assertion"),
    STRESS_REPORT_EXISTING(260009, "The pressure test is already running, please do not repeat it"),
    /**
     * API操作
     */
    API_OPERATION_UNSUPPORTED_FROM(230004, "Unsupported source operations"),
    API_OPERATION_UNSUPPORTED_ASSERTION(230005, "Unsupported assertion operations"),
    API_OPERATION_UNSUPPORTED_RELATION(230006, "Unsupported associated values"),
    API_RELATION_NOT_EXIST(230007, "Associated parameter does not exist"),
    API_ASSERTION_FAILED(230008, "API assertion failed"),
    API_FILE_NOT_EXIST(230010, "API uploaded file does not exist"),
    API_CASE_STEP_IS_EMPTY(280404, "API test case step does not exist"),

    UI_UNSUPPORTED_BROWSER_DRIVER(401000, "Browser driver is not supported"),
    UI_ELEMENT_NOT_EXIST(401404, "Element locator does not exist"),
    UI_OPERATION_UNSUPPORTED(401405, "Operation type is not supported"),
    UI_OPERATION_UNSUPPORTED_BROWSER(401406, "Browser operation is not supported"),
    UI_OPERATION_UNSUPPORTED_MOUSE(401407, "Mouse operation is not supported"),
    UI_OPERATION_UNSUPPORTED_KEYBOARD(401408, "Keyboard operation is not supported"),
    UI_OPERATION_UNSUPPORTED_WAIT(401409, "Wait operation is not supported"),
    UI_OPERATION_UNSUPPORTED_ASSERTION(401410, "Assertion type is not supported"),

    TEST_TYPE_UNSUPPORTED(504404, "Test type is not supported"),
    UI_UNSUPPORTED_LOCATION_TYPE(504405,"Ui test type is not supported" ),


    AUTH_NOT_LOGIN(401, "Not logged in or login expired, please log in again"),
    AUTH_NO_PERMISSION(403, "Sorry, you do not have permission to operate this interface"),
    SERVER_ERROR(500, "The system is malfunctioning, please try again later");




    @Getter
    private String message;
    @Getter
    private int code;

    private BizCodeEnum(int code, String message){
        this.code = code;
        this.message = message;
    }
}
