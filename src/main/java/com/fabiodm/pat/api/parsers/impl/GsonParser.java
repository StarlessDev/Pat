package com.fabiodm.pat.api.parsers.impl;

import com.fabiodm.pat.api.event.PatEvent;
import com.fabiodm.pat.api.parsers.PatParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.Optional;

/*
* Class responsible for parsing PatEvent messages into JsonElement objects using Gson.
* */
public class GsonParser extends PatParser<JsonElement> {

    public GsonParser(PatEvent event) {
        super(event);
    }

    @Override
    public Optional<JsonElement> asObject() {
        final String rawJson = this.event.messageAsString();
        try {
            return Optional.of(JsonParser.parseString(rawJson));
        } catch (JsonSyntaxException ex) {
            return Optional.empty();
        }
    }
}
