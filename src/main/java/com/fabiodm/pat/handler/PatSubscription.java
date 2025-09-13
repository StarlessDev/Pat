package com.fabiodm.pat.handler;

import com.fabiodm.pat.api.event.PatEvent;

public abstract class PatSubscription {

    public abstract void handle(final Object listener, final PatEvent event);
}
