package com.fabiodm.pat.handler;

import com.fabiodm.pat.api.PatSubscribe;
import com.fabiodm.pat.api.event.PatEvent;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * This class represents a method that is annotated with the PatSubscribe annotation.
 * It holds a reference to the Method object and the channel name extracted from the annotation.
 */
record PatMethod(Method method, String channel) {

    /**
     * Creates a PatMethod instance from a given Method object.
     * The method must be annotated with PatSubscribe and have a single parameter of type PatEvent.
     * If these conditions are not met, an empty Optional is returned.
     *
     * @param method the Method object to create a PatMethod from
     * @return an Optional containing a PatMethod if the conditions are met, otherwise an empty Optional
     */
    public static Optional<PatMethod> fromMethod(final Method method) {
        final PatSubscribe annotation = method.getAnnotation(PatSubscribe.class);
        if (annotation != null) {
            final Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length == 1 && parameters[0].equals(PatEvent.class)) {
                return Optional.of(new PatMethod(method, annotation.value()));
            }
        }
        return Optional.empty();
    }
}