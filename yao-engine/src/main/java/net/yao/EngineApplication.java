package net.yao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@MapperScan("net.yao.mapper")
public class EngineApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(EngineApplication.class, args);

    }
}
