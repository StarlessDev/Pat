package com.fabiodm.pat.api.event;

import com.fabiodm.pat.api.parsers.ProtobufParser;
import com.google.protobuf.Parser;

import java.util.Optional;

/**
 * This class represents an event in the Pat system.
 * It holds a reference to the channel name and the message as a byte array.
 */
public record PatEvent(String channel, byte[] message) {

    public <T> ProtobufParser<T> asProtobufParser(final Parser<T> parser) {
        return new ProtobufParser<>(this, parser);
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