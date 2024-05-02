package com.fabiodm.pat.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark methods that should be invoked when a message is received on a specific channel.
 * The channel is specified as a parameter to the annotation.
 * The annotated methods should be part of a class that is registered as a listener in the Pat system.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PatSubscribe {
    /**
     * The channel that the annotated method is subscribed to.
     *
     * @return the name of the channel
     */
    String value();
}