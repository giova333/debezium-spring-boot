package com.gladunalexander.debezium.core;

import com.gladunalexander.debezium.autoconfigure.DebeziumProperties;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import org.glassfish.jersey.internal.guava.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class DebeziumEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DebeziumEventHandler.class);

    private final DebeziumProperties debeziumProperties;
    private final List<DebeziumEventListenerMetadata> listenerMetadata = new ArrayList<>();
    private final Executor executor;
    private DebeziumEngine<ChangeEvent<String, String>> debeziumEngine;

    DebeziumEventHandler(DebeziumProperties debeziumProperties) {
        this.debeziumProperties = debeziumProperties;

        executor = Executors.newSingleThreadExecutor(
                new ThreadFactoryBuilder().setNameFormat("debezium-thread-%d").build()
        );
    }

    void register(DebeziumEventListenerMetadata metadata) {
        this.listenerMetadata.add(metadata);
    }

    void start() {
        if (!listenerMetadata.isEmpty()) {
            listenerMetadata.sort(
                    (m1, m2) -> AnnotationAwareOrderComparator.INSTANCE.compare(
                            m1.getTargetMethod(),
                            m2.getTargetMethod()
                    )
            );

            LOG.debug("Starting debezium engine");
            this.debeziumEngine = DebeziumEngine.create(Json.class)
                    .using(debeziumProperties.getProperties())
                    .notifying(this::handleEvent)
                    .build();
            this.executor.execute(debeziumEngine);
        }
    }

    void destroy() {
        if (debeziumEngine != null) {
            try {
                LOG.debug("Closing debezium engine");
                debeziumEngine.close();
            } catch (IOException e) {
                throw new RuntimeException("Unable to close debezium engine", e);
            }
        }
    }

    private void handleEvent(ChangeEvent<String, String> changeEvent) {
        listenerMetadata.forEach(metadata ->
                ReflectionUtils.invokeMethod(metadata.getTargetMethod(), metadata.getTarget(), changeEvent));
    }

}
