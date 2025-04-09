package com.easylive.web;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.easylive"},exclude = {DataSourceAutoConfiguration.class})
public class EasyLiveRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasyLiveRunApplication.class, args);
    }
}
