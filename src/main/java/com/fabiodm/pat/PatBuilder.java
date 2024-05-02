package com.fabiodm.pat;

import com.fabiodm.pat.api.PatClient;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.codec.CompressionCodec;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * This class is used to create instances of the Pat class using the builder pattern.
 * It provides a static factory method for creating a PatBuilder instance and methods to set the RedisURI and ClientOptions.
 */
public final class PatBuilder {

    private final RedisURI redisURI;
    private ClientOptions clientOptions;

    private CompressionCodec.CompressionType compressionType;

    /**
     * Private constructor to prevent direct instantiation.
     * Initializes the clientOptions with default values.
     */
    private PatBuilder(final RedisURI redisURI) {
        this.redisURI = redisURI;
        this.clientOptions = ClientOptions.builder()
            .autoReconnect(true)
            .pingBeforeActivateConnection(true)
            .scriptCharset(StandardCharsets.UTF_8)
            .timeoutOptions(TimeoutOptions.builder()
                .fixedTimeout(Duration.ofSeconds(3))
                .build())
            .build();
    }

    /**
     * Static factory method to create a new PatBuilder instance.
     *
     * @return a new PatBuilder instance
     */
    public static PatBuilder create(final RedisURI redisURI) {
        return new PatBuilder(redisURI);
    }

    /**
     * Sets the ClientOptions for the Pat instance to be built.
     *
     * @param clientOptions the ClientOptions to be used by the Pat instance
     * @return the current PatBuilder instance
     */
    public PatBuilder withClientOptions(final ClientOptions clientOptions) {
        this.clientOptions = clientOptions;
        return this;
    }

    /**
     * Sets the compression type for the Pat instance to be built.
     *
     * @param compressionType the CompressionType to be used by the Pat instance
     * @return the current PatBuilder instance
     */
    public PatBuilder withCompression(final CompressionCodec.CompressionType compressionType) {
        this.compressionType = compressionType;
        return this;
    }

    /**
     * Builds a new Pat instance with the set RedisURI and ClientOptions.
     * If either RedisURI or ClientOptions is not set before calling this method, it throws an IllegalArgumentException.
     *
     * @return a new Pat instance
     * @throws IllegalArgumentException if either RedisURI or ClientOptions is not set
     */
    public PatClient build() {
        if (this.redisURI == null || this.clientOptions == null) {
            throw new IllegalArgumentException("RedisURI and ClientOptions must be set before building Pat");
        }

        return new Pat(this.redisURI, this.clientOptions, this.compressionType);
    }
}