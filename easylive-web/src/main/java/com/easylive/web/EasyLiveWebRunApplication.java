package com.easylive.web;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.easylive"})
@MapperScan(basePackages = ("cpm.easylive.mappers"))
public class EasyLiveWebRunApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasyLiveWebRunApplication.class, args);
    }
}
