package net.yao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@MapperScan({
        "net.yao.gatewaymapper",
})
@EnableDiscoveryClient
public class GatewayApplication {
    public static void main(String[] args) {

         SpringApplication.run(GatewayApplication.class, args);
    }
}
