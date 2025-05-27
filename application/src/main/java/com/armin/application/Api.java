package com.armin.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.TimeZone;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@EnableAspectJAutoProxy
@EnableAsync
@EnableScheduling
@EntityScan(basePackages = {"com.armin.*"})
@ComponentScan(basePackages = {"com.armin.utility.*", "com.armin.*"})
@ConfigurationPropertiesScan(basePackages = {"com.armin.*"})
@EnableFeignClients(basePackages = "com.armin.*")
public class Api {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(Api.class, args);
    }
}
