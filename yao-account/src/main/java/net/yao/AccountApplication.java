package net.yao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients
@EnableDiscoveryClient
@EnableAsync
@MapperScan("net.yao.accountmapper")
public class AccountApplication {
    public static void main(String[] args) {

        SpringApplication.run(AccountApplication.class, args);
    }
}
