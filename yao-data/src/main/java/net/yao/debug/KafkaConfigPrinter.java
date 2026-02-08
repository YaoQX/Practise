package net.yao.debug;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaConfigPrinter {

    // 读取 spring.kafka.bootstrap-servers
    @Value("${spring.kafka.bootstrap-servers:NOT_SET}")
    private String bootstrapServers;

    @PostConstruct
    public void printKafkaConfig() {
        System.err.println("========================================");
        System.err.println("### Kafka bootstrap-servers = " + bootstrapServers);
        System.err.println("========================================");
    }
}
