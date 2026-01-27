package com.fabiodm.pat.api.parsers.impl;

import com.fabiodm.pat.api.event.PatEvent;
import com.fabiodm.pat.api.parsers.PatParser;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;

import java.util.Optional;

public class ProtobufParser<T> extends PatParser<T> {

    private final Parser<T> parser;

    public ProtobufParser(final PatEvent event, final Parser<T> parser) {
        super(event);
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
