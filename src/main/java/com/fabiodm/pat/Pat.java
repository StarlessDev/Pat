package com.fabiodm.pat;

import com.fabiodm.pat.api.PatClient;
import com.fabiodm.pat.api.event.PatEvent;
import com.fabiodm.pat.codec.ByteArrayCodec;
import com.fabiodm.pat.exception.PatRegistrationException;
import com.fabiodm.pat.handler.PatHandler;
import com.google.protobuf.GeneratedMessage;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.codec.CompressionCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * This class implements the PatClient interface and provides methods for connecting to a Redis server,
 * sending and receiving messages, and managing listeners for messages.
 */
final class Pat implements PatClient {

    // The Redis client for connecting to the Redis server.
    private final RedisClient redisClient;
    // The connection to the Redis server.
    private StatefulRedisPubSubConnection<String, byte[]> connection;

    // The compression type for the messages.
    private final CompressionCodec.CompressionType compressionType;

    // The listener for Redis pub/sub messages.
    private final PatListener patListener;

    // The set of listeners for PatEvents.
    private final Set<PatHandler> listeners = new HashSet<>();

    /**
     * Constructs a Pat object with the given RedisURI and ClientOptions.
     *
     * @param URI     the RedisURI for the Redis server
     * @param options the ClientOptions for the Redis client
     */
    Pat(final RedisURI URI, final ClientOptions options, final CompressionCodec.CompressionType compressionType) {
        this.redisClient = RedisClient.create(URI);
        this.redisClient.setOptions(options);

        this.compressionType = compressionType;

        this.patListener = new PatListener(this);
    }

    @Override
    public void connect() {
        if (!this.isConnected()) {
            final RedisCodec<String, byte[]> codec;
            if (this.compressionType != null) {
                codec = CompressionCodec.valueCompressor(new ByteArrayCodec(), this.compressionType);
            } else {
                codec = new ByteArrayCodec();
            }

            this.connection = this.redisClient.connectPubSub(codec);
            this.connection.addListener(this.patListener);
        }
    }

    @Override
    public void disconnect() {
        if (this.isConnected()) {
            this.connection.removeListener(this.patListener);
            this.connection.close();
        }
    }

    @Override
    public void shutdown() {
        this.disconnect();
        this.redisClient.shutdown();
    }

    @Override
    public void register(final Object object) {
        final PatHandler patHandler = new PatHandler(object);
        if (!patHandler.isEmpty()) {
            for (final String channel : patHandler.getChannels()) {
                if (this.listeners.stream().anyMatch(handler -> handler.getChannels().contains(channel))) {
                    throw new PatRegistrationException("Duplicate subscription for channel '" + channel + "' in class '" + object.getClass().getSimpleName() + "'");
                }
            }

            if (this.listeners.add(patHandler)) {
                patHandler.getChannels().forEach(this::subscribe);
            }
        }
    }

    @Override
    public void unregister(final Object object) {
        this.listeners.stream()
            .filter(patHandler -> patHandler.isListener(object))
            .findFirst()
            .ifPresent(patHandler -> {
                this.listeners.remove(patHandler);
                patHandler.getChannels().forEach(this::unsubscribe);
            });
    }

    @Override
    public void send(final String channel, final byte[] message) {
        this.connection.sync().publish(channel, message);
    }

    @Override
    public void send(final String channel, final String message) {
        this.send(channel, message.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void send(final String channel, final GeneratedMessage message) {
        this.send(channel, message.toByteArray());
    }

    @Override
    public boolean isConnected() {
        return this.connection != null && this.connection.isOpen();
    }

    /**
     * Subscribes to a Redis pub/sub channel.
     *
     * @param channel the channel to subscribe to
     */
    private void subscribe(final String channel) {
        this.connection.sync().subscribe(channel);
    }

    /**
     * Unsubscribes from a Redis pub/sub channel.
     *
     * @param channel the channel to unsubscribe from
     */
    private void unsubscribe(final String channel) {
        this.connection.sync().unsubscribe(channel);
    }

    /**
     * Broadcasts a PatEvent to all listeners.
     *
     * @param event the PatEvent to broadcast
     */
    void broadcast(final PatEvent event) {
        this.listeners.forEach(listener -> listener.handle(event));
    }
}