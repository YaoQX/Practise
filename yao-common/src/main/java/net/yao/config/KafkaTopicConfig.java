package net.yao.config;

public class KafkaTopicConfig {

    /**
     * 压测 专门接收压力测试产生的报告数据。
     */
    public static final String STRESS_TOPIC_NAME = "stress_report_topic";

    /**
     * 接口自动化 接收接口自动化测试的结果。
     */
    public static final String API_TOPIC_NAME = "api_report_topic";

    /**
     * ui自动化 接收UI 自动化测试（如 Selenium 或 Appium）的结果。
     */
    public static final String UI_TOPIC_NAME = "ui_report_topic";

    /**
     * 报告状态的topic
     */
    public static final String REPORT_STATE_TOPIC_NAME = "report_state_topic";

}
