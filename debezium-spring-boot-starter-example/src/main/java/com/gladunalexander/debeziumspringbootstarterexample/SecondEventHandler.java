package com.gladunalexander.debeziumspringbootstarterexample;

import com.gladunalexander.debezium.annotation.DebeziumEventListener;
import io.debezium.engine.ChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecondEventHandler {

    @Order(2)
    @DebeziumEventListener
    public void listen1(ChangeEvent<String, String> changeEvent) {
        log.info("Listener 3: {}", changeEvent);
    }

}
