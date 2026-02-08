package net.yao.stress;

import net.yao.config.KafkaTopicConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
public class KafkaTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;



    @Test
    public void testSendMsg() throws Exception {
        //kafkaTemplate.send(KafkaTopicConfig.STRESS_TOPIC_NAME,"case_id_"+1,"Kafka Test");
//        kafkaTemplate.send(KafkaTopicConfig.STRESS_TOPIC_NAME, "case_id_"+1, "Kafka Test Data: " + System.currentTimeMillis())
//                .whenComplete((result, ex) -> {
//                    if (ex == null) {
//                        System.out.println(">>> Send Success Offset: " + result.getRecordMetadata().offset());
//                    } else {
//                        System.err.println(">>> Send error: " + ex.getMessage());
//                        ex.printStackTrace();
//                    }
//                });

        var future = kafkaTemplate.send(
                KafkaTopicConfig.STRESS_TOPIC_NAME,
                //"stress_report_topic",
                "case_id_" + 1,
                "Kafka Test Data: " + System.currentTimeMillis()
        );

        var result = future.get(); // 阻塞等待 broker ack
        System.out.println(">>> Send Success Offset: " + result.getRecordMetadata().offset());

    }

}
