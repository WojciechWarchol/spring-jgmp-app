package com.wojto;

import com.wojto.exception.handler.CustomHandlerExceptionResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerExceptionResolver;

@SpringBootApplication
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("com.wojto.dao")
@EntityScan("com.wojto.model")
@EnableTransactionManagement
@EnableCaching
public class EventAppConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventAppConfig.class);

    public static void main(String[] args) {
        SpringApplication.run(EventAppConfig.class, args);
    }

    @Bean
    public HandlerExceptionResolver customHandlerExceptionResolver() {
        return new CustomHandlerExceptionResolver();
    }
}
