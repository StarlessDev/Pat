package com.fabiodm.pat.api.parsers;

import com.fabiodm.pat.api.event.PatEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;

import java.util.Optional;

public class ProtobufParser<T> {

    private final PatEvent event;
    private final Parser<T> parser;

    public ProtobufParser(final PatEvent event, final Parser<T> parser) {
        this.event = event;
        this.parser = parser;
    }

    public Optional<T> asObject() {
        try {
            return Optional.ofNullable(parser.parseFrom(this.event.message()));
        } catch (final InvalidProtocolBufferException e) {
            return Optional.empty();
        }
    }
}
