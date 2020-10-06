package com.gladunalexander.debezium.autoconfigure;

import com.gladunalexander.debezium.core.DebeziumEventListenerMethodProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DebeziumProperties.class)
public class DebeziumAutoconfiguration {

    @Bean
    public DebeziumEventListenerMethodProcessor methodProcessor(DebeziumProperties properties) {
        return new DebeziumEventListenerMethodProcessor(properties);
    }
}
