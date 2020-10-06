package com.gladunalexander.debezium.autoconfigure;

import com.gladunalexander.debezium.core.ArgumentNotOfRequiredTypeException;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

public class ArgumentNotOfRequiredTypeFailureAnalyzer extends AbstractFailureAnalyzer<ArgumentNotOfRequiredTypeException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, ArgumentNotOfRequiredTypeException cause) {
        return new FailureAnalysis(getDescription(cause), getAction(cause), cause);
    }

    private String getDescription(ArgumentNotOfRequiredTypeException ex) {
        return String.format(
                "The method %s.%s could not be invoked. It must have only one argument of type %s",
                ex.getTargetClass().getName(),
                ex.getMethod().getName(),
                ex.getExpectedType()
        );
    }

    private String getAction(ArgumentNotOfRequiredTypeException ex) {
        return String.format("Consider changing the method with name %s.%s and keep only parameter of type %s",
                ex.getTargetClass().getName(),
                ex.getMethod().getName(),
                ex.getExpectedType()
        );
    }
}
