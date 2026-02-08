package net.yao;

import jakarta.annotation.PostConstruct;
import net.yao.config.KafkaTopicConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("net.yao.mapper")
@EnableTransactionManagement
@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan(basePackages = "net.yao")
public class DataApplication {
    public static void main(String[] args) {


        SpringApplication.run(DataApplication.class, args);
    }
}
