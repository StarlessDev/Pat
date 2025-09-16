package com.fabiodm.pat.handler.impl;

import com.fabiodm.pat.api.event.PatEvent;
import com.fabiodm.pat.exception.PatEventInvocationException;
import com.fabiodm.pat.exception.PatRegistrationException;
import com.fabiodm.pat.handler.PatSubscription;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnnotatedSubscription extends PatSubscription {

    private final Method handlerMethod;

    public AnnotatedSubscription(Method handlerMethod) {
        this.handlerMethod = handlerMethod;
        if (!this.handlerMethod.trySetAccessible()) {
            throw new PatRegistrationException(String.format("Handler method %s from class %s not accessible.",
                    handlerMethod.getName(),
                    handlerMethod.getDeclaringClass().getSimpleName()
            ));
        }
    }

    @Override
    public void handle(final Object listener, final PatEvent event) {
        try {
            this.handlerMethod.invoke(listener, event);
        } catch (final InvocationTargetException | IllegalAccessException e) {
            throw new PatEventInvocationException(String.format("Error invoking handler method %s from class %s.",
                    handlerMethod.getName(),
                    handlerMethod.getDeclaringClass().getSimpleName()
            ), e);
        }
    }
}
