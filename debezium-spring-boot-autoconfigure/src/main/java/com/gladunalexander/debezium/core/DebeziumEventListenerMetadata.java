package com.gladunalexander.debezium.core;

import java.lang.reflect.Method;

class DebeziumEventListenerMetadata {

    private final Object target;
    private final Method targetMethod;

    DebeziumEventListenerMetadata(Object target, Method targetMethods) {
        this.target = target;
        this.targetMethod = targetMethods;
    }

    Object getTarget() {
        return target;
    }

    Method getTargetMethod() {
        return targetMethod;
    }
}
