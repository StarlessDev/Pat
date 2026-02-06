package com.fabiodm.pat.api.event;

import com.fabiodm.pat.api.parsers.PatParser;
import com.fabiodm.pat.api.parsers.impl.GsonParser;
import com.fabiodm.pat.api.parsers.impl.ProtobufParser;
import com.google.gson.JsonElement;
import com.google.protobuf.Parser;

import java.util.Optional;

/**
 * This class represents an event in the Pat system.
 * It holds a reference to the channel name and the message as a byte array.
 * @param channel the channel name
 * @param message the message
 */
public record PatEvent(String channel, byte[] message) {

    public <T> PatParser<T> asProtobufParser(final Parser<T> parser) {
        return new ProtobufParser<>(this, parser);
    }

    public PatParser<JsonElement> asGsonParser() {
        return new GsonParser(this);
    }

    /**
     * Returns the message as a String.
     * If the message is null, null is returned.
     *
     * @return the message as a String, or null if the message is null
     */
    public String messageAsString() {
        return Optional.ofNullable(this.message)
                .map(String::new)
                .orElse(null);
    }
}