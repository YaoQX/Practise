package net.yao.stress;

import net.yao.config.KafkaTopicConfig;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;

public class RawKafkaTest {
    public static void main(String[] args) {
        Properties props = new Properties();
        // 你的 Kafka 服务器地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "18.181.159.177:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // 这里的 Topic 名字必须和你消费者监听的一模一样
        //String topic = KafkaTopicConfig.STRESS_TOPIC_NAME;
        String topic = "stress_report_topic";
        String message = "{\"id\":1234, \"content\":\"手动测试消息\"}";

//        producer.send(new ProducerRecord<>(topic, message), (metadata, exception) -> {
//            if (exception == null) {
//                System.out.println("✅ 发送成功！位移为: " + metadata.offset());
//            } else {
//                System.err.println("❌ 发送失败！");
//                exception.printStackTrace();
//            }
//        });

        producer.send(new ProducerRecord<>(topic, message), (meta, ex) -> {
            if (ex != null) {
                System.err.println("❌ 发送失败：" + ex.getMessage());
            } else {
                System.out.println("✅ 发送成功！已进入分区: " + meta.partition() + "，位移: " + meta.offset());
            }
        });

        producer.flush();
        producer.close();
    }
}