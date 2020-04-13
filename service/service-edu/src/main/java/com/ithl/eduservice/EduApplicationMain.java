package com.ithl.eduservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hl
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.ithl"})
public class EduApplicationMain {
    public static void main(String[] args) {
        SpringApplication.run(EduApplicationMain.class, args);
    }
}
