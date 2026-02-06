package com.fabiodm.pat.handler;

import com.fabiodm.pat.api.event.PatEvent;

/*
* Class representing a subscription to a PatEvent.
* */
public abstract class PatSubscription {

    /*
    * Method invoked when the PatEvent is handled.
    * */
    public abstract void handle(final Object listener, final PatEvent event);
}
