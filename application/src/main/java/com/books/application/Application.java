package com.books.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.TimeZone;

@SpringBootApplication
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableAsync
@EnableScheduling
@EntityScan(basePackages = {"com.books.*"})
@ComponentScan(basePackages = {"com.books.utility.*", "com.books.*"})
public class Application {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(Application.class, args);
    }
}
