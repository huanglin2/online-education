package com.ithl.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author hl
 * @Data 2020/4/30
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
public class GatewayApplicationMain8222 {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplicationMain8222.class, args);
    }
}
