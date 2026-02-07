package com.fabiodm.pat.handler.impl;

import com.fabiodm.pat.api.event.PatEvent;
import com.fabiodm.pat.handler.PatSubscription;

import java.util.function.Consumer;

/*
 * Class representing a subscription to a PatEvent that is handled by a Consumer.
 * */
public class ConsumerSubscription extends PatSubscription {

    /*
     * Consumer that handles the PatEvent.
     * */
    private final Consumer<PatEvent> consumer;

    public ConsumerSubscription(Consumer<PatEvent> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void handle(Object listener, PatEvent event) {
        this.consumer.accept(event);
    }
}
