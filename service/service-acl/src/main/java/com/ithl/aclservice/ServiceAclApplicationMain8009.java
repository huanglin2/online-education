package com.ithl.aclservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.ithl")
@MapperScan("com.ithl.aclservice.mapper")
public class ServiceAclApplicationMain8009 {

    public static void main(String[] args) {
        SpringApplication.run(ServiceAclApplicationMain8009.class, args);
    }

}
