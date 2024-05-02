package com.fabiodm.pat.handler;

import com.fabiodm.pat.api.event.PatEvent;
import com.fabiodm.pat.exception.PatEventInvocationException;
import com.fabiodm.pat.exception.PatRegistrationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a listener for PatEvents.
 * It holds a reference to the listener object and a map of methods that are annotated with PatSubscribe.
 */
public final class PatHandler {

    private final Object listener;
    private final Map<String, PatMethod> channels = new HashMap<>();

    /**
     * Constructs a PatHandler with the given listener object.
     * It also initializes the methods map with methods from the listener object that are annotated with PatSubscribe.
     *
     * @param listener the listener object
     */
    public PatHandler(final Object listener) {
        this.listener = listener;
        for (final Method method : listener.getClass().getDeclaredMethods()) {
            PatMethod.fromMethod(method).ifPresent(patMethod -> {
                if (this.channels.containsKey(patMethod.channel())) {
                    throw new PatRegistrationException("Duplicate subscription for channel '" + patMethod.channel() + "' in class '" + this.listener.getClass().getSimpleName() + "'");
                }
                this.channels.put(patMethod.channel(), patMethod);
            });
        }
    }

    /**
     * Handles a PatEvent by invoking the corresponding method in the listener object.
     *
     * @param event the PatEvent to handle
     */
    public void handle(final PatEvent event) {
        final PatMethod patMethod = this.channels.get(event.channel());
        if (patMethod != null && patMethod.method().trySetAccessible()) {
            try {
                patMethod.method().invoke(this.listener, event);
            } catch (final InvocationTargetException | IllegalAccessException e) {
                throw new PatEventInvocationException("Error invoking method for channel '" + event.channel() + "' in class '" + this.listener.getClass().getSimpleName() + "'");
            }
        }
    }

    /**
     * Returns a set of the channels in the methods map.
     *
     * @return a set of the channels in the methods map
     */
    public Set<String> getChannels() {
        return this.channels.keySet();
    }

    /**
     * Checks if the methods map is empty.
     *
     * @return true if the methods map is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.channels.isEmpty();
    }

    /**
     * Checks if the given object is the listener object of this PatHandler.
     *
     * @param object the object to check
     * @return true if the given object is the listener object, false otherwise
     */
    public boolean isListener(final Object object) {
        return this.listener == object;
    }
}