package com.gladunalexander.debezium.core;

import com.gladunalexander.debezium.annotation.DebeziumEventListener;
import com.gladunalexander.debezium.autoconfigure.DebeziumProperties;
import io.debezium.engine.ChangeEvent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Set;

import static org.springframework.util.ReflectionUtils.MethodFilter;

public class DebeziumEventListenerMethodProcessor
        implements BeanPostProcessor, SmartInitializingSingleton, DisposableBean {

    private final DebeziumEventHandler debeziumEventHandler;

    public DebeziumEventListenerMethodProcessor(DebeziumProperties debeziumProperties) {
        this.debeziumEventHandler = new DebeziumEventHandler(debeziumProperties);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Set<Method> methods = MethodIntrospector.selectMethods(
                bean.getClass(),
                (MethodFilter) method -> method.isAnnotationPresent(DebeziumEventListener.class)
        );

        for (Method method : methods) {
            if (listenerHasInvalidArgument(method)) {
                throw new ArgumentNotOfRequiredTypeException(
                        ChangeEvent.class, bean.getClass(), method
                );
            }
            debeziumEventHandler.register(new DebeziumEventListenerMetadata(bean, method));
        }

        return bean;
    }

    @Override
    public void afterSingletonsInstantiated() {
        debeziumEventHandler.start();
    }

    @Override
    public void destroy() {
        debeziumEventHandler.destroy();
    }

    private boolean listenerHasInvalidArgument(Method targetMethod) {
        return targetMethod.getParameters().length != 1
                || !ClassUtils.isAssignable(ChangeEvent.class, targetMethod.getParameters()[0].getType());

    }
}
