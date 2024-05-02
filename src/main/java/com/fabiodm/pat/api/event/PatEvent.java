package com.fabiodm.pat.api.event;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;

import java.util.Optional;

/**
 * This class represents an event in the Pat system.
 * It holds a reference to the channel name and the message as a byte array.
 */
public record PatEvent(String channel, byte[] message) {

    /**
     * Serializes the message into a Protocol Buffers message.
     * If the serialization fails, an empty Optional is returned.
     *
     * @param parser the Protocol Buffers parser to use for serialization
     * @param <T>    the type of the Protocol Buffers message
     * @return an Optional containing the serialized message if successful, otherwise an empty Optional
     */
    public <T> Optional<T> serializeProto(final Parser<T> parser) {
        try {
            return Optional.ofNullable(parser.parseFrom(this.message));
        } catch (final InvalidProtocolBufferException e) {
            return Optional.empty();
        }
    }

    /**
     * Returns the message as a String.
     * If the message is null, null is returned.
     *
     * @return the message as a String, or null if the message is null
     */
    public String messageString() {
        return Optional.ofNullable(this.message)
            .map(String::new)
            .orElse(null);
    }
}