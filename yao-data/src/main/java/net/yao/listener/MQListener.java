package net.yao.listener;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.yao.config.KafkaTopicConfig;
import net.yao.req.ReportUpdateReq;
import net.yao.service.ReportDetailService;
import net.yao.service.ReportService;
import net.yao.util.JsonUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MQListener {


    @Resource
    private ReportDetailService reportDetailService;


    @Resource
    private ReportService reportService;


    /**
     * 消费监听，压测日志详情
     * 指定监听 report_state_topic。这意味着每当有压测任务的状态发生变化（比如“开始”、“完成”），这个方法就会被触发。
     * 同一个组内的消费者会共同平摊这个 Topic 的消息
     * @param record
     * @param ack
     */
    @KafkaListener(topics = {KafkaTopicConfig.STRESS_TOPIC_NAME} ,groupId = "debug-group-fixed-83")
    public void onStressReportDetailMessage(ConsumerRecord<String, String> record, Acknowledgment ack,  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        //打印消息
        //System.err.println("🔥 抓到了！收到消息：" + record.value()); // 用 err 输出红色文字
        log.info("Consumer topic：{},Partition：{} Get message：{}",record.topic(),record.partition(),record.value());
        reportDetailService.handleStressReportDetail(record.value().toString());

        //必须执行
        ack.acknowledge();

    }

    /**
     * 消费监听，处理报告的状态
     * @param record
     * @param ack
     * @param topic
     */
    @KafkaListener(topics = {KafkaTopicConfig.REPORT_STATE_TOPIC_NAME},groupId = "yao-report-test-gp6")
    public void onStressReportStateMessage(ConsumerRecord<?,?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        //打印消息

        log.info("Consumer topic：{},Consumer topic：{} Get message：{}",record.topic(),record.partition(),record.value());
        ReportUpdateReq reportUpdateReq = JsonUtil.json2Obj(record.value().toString(), ReportUpdateReq.class);

        reportService.updateReportState(reportUpdateReq);
        ack.acknowledge();
    }

    /**
     * 消费监听，接口自动化测试日志详情
     * @param record
     * @param ack
     * @param topic
     */
    @KafkaListener(topics = {KafkaTopicConfig.API_TOPIC_NAME},groupId = "yao-api-test-gp")
    public void onApiReportDetailMessage(ConsumerRecord<?,?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        //打印消息
        log.info("Consumer topic：{},Consumer topic：{} Get message：{}",record.topic(),record.partition(),record.value());
        reportDetailService.handleApiReportDetail(record.value().toString());
        ack.acknowledge();
    }

    /**
     * 消费监听，UI自动化测试日志详情
     * @param record
     * @param ack
     * @param topic
     */
    @KafkaListener(topics = {KafkaTopicConfig.UI_TOPIC_NAME},groupId = "yao-ui-test-gp")
    public void onUiReportDetailMessage(ConsumerRecord<?,?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        //打印消息
        log.info("Consumer topic：{},Consumer topic：{} Get message：{}",record.topic(),record.partition(),record.value());
        reportDetailService.handleUiReportDetail(record.value().toString());
        ack.acknowledge();
    }




}
