package com.fabiodm.pat.api.parsers;

import com.fabiodm.pat.api.event.PatEvent;

import java.util.Optional;

public abstract class PatParser<T> {

    protected final PatEvent event;

    public PatParser(final PatEvent event) {
        this.event = event;
    }

    public abstract Optional<T> asObject();
}
