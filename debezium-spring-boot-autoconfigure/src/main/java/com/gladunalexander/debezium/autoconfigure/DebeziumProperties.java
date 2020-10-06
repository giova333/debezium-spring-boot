package com.gladunalexander.debezium.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Properties;

@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "debezium")
public class DebeziumProperties {

    @NotNull
    private final Properties properties;

    public DebeziumProperties(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }
}
