package com.gladunalexander.debezium.core;

import java.lang.reflect.Method;

public class ArgumentNotOfRequiredTypeException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Invalid argument of the method %s.%s. Expected type is %s";

    private final Class<?> expectedType;
    private final Class<?> targetClass;
    private final Method method;

    public ArgumentNotOfRequiredTypeException(Class<?> expectedType, Class<?> targetClass, Method method) {
        super(String.format(ERROR_MESSAGE, targetClass.getName(), method.getName(), expectedType.getName()));
        this.expectedType = expectedType;
        this.targetClass = targetClass;
        this.method = method;
    }

    public Class<?> getExpectedType() {
        return expectedType;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }
}
