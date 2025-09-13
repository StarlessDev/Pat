package com.fabiodm.pat.handler;

import com.fabiodm.pat.Pat;
import com.fabiodm.pat.api.PatSubscribe;
import com.fabiodm.pat.api.event.PatEvent;
import com.fabiodm.pat.exception.PatEventInvocationException;
import com.fabiodm.pat.exception.PatRegistrationException;
import com.fabiodm.pat.handler.impl.AnnotatedSubscription;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class represents a listener for PatEvents.
 * It holds a reference to the listener object and a map of methods that are annotated with PatSubscribe.
 */
public final class PatHandler {

    private final Object listener;
    private final Map<String, List<PatSubscription>> channels = new ConcurrentHashMap<>();

    /**
     * Constructs a PatHandler with the given listener object.
     * It also initializes the methods map with methods from the listener object that are annotated with PatSubscribe.
     *
     * @param listener the listener object
     */
    public PatHandler(final Object listener) {
        this.listener = listener;
        this.registerAnnotatedSubscriptions();
    }

    private void registerAnnotatedSubscriptions() {
        for (final Method method : listener.getClass().getMethods()) {
            final String subscriptionChannel = this.getSubscriptionFromMethod(method);
            if (subscriptionChannel != null) {
                try {
                    this.registerSubscription(subscriptionChannel, new AnnotatedSubscription(method));
                } catch (final PatRegistrationException e) {
                    Pat.LOGGER.error("An error occurred while registering a listener: ", e);
                }
            }
        }
    }

    private String getSubscriptionFromMethod(final Method method) {
        final PatSubscribe annotation = method.getAnnotation(PatSubscribe.class);
        if (annotation != null) {
            final Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length == 1 && parameters[0].equals(PatEvent.class)) {
                return annotation.value();
            }
        }
        return null;
    }

    public void registerSubscription(final String channel,
                                     final PatSubscription subscription) {
        this.channels.computeIfAbsent(channel, k -> new CopyOnWriteArrayList<>()).add(subscription);
    }

    /**
     * Handles a PatEvent by invoking the corresponding method in the listener object.
     *
     * @param event the PatEvent to handle
     */
    public void handle(final PatEvent event) {
        for (final PatSubscription subscription : this.channels.get(event.channel())) {
            try {
                subscription.handle(listener, event);
            } catch (final PatEventInvocationException e) {
                Pat.LOGGER.error(e.getMessage(), e.getCause());
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PatHandler that = (PatHandler) o;
        return Objects.equals(listener.getClass(), that.listener.getClass());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(listener);
    }
}