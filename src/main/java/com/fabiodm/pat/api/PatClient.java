package com.fabiodm.pat.api;

import com.fabiodm.pat.api.event.PatEvent;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;

import java.util.function.Consumer;

/**
 * This interface defines the operations for the Pat system.
 * It provides methods for connecting to and disconnecting from the server,
 * registering and unregistering listeners, sending messages, and checking the connection status.
 */
public interface PatClient {

    /**
     * Connects to the database.
     */
    void connect();

    /**
     * Disconnects from the database.
     */
    void disconnect();

    /**
     * Shuts down the database.
     */
    void shutdown();

    /**
     * Registers a listener.
     *
     * @param object the listener object
     */
    void register(final Object object);

    /**
     * Unregisters a listener.
     *
     * @param object the listener object
     */
    void unregister(final Object object);

    /**
     * Uses an already registered listener to subscribe a
     * consumer to a specific redis pubsub channel.
     * <p>
     * Remember that this method will be unregistered
     * only when the entire listener is unregistered.
     *
     * @param listener an already registered listener
     * @param channel  the channel to subscribe to
     * @param consumer the consumer to handle the messages
     */
    void subscribeToChannel(final Object listener,
                            final String channel,
                            final Consumer<PatEvent> consumer);

    /**
     * Sends a message to a channel synchronously.
     * The message is a byte array.
     *
     * @param channel the channel to send the message to
     * @param message the message
     */
    void send(final String channel, final byte[] message);

    /**
     * Sends a message to a channel synchronously.
     * The message is a String.
     *
     * @param channel the channel to send the message to
     * @param message the message
     */
    void send(final String channel, final String message);

    /**
     * Sends a message to a channel asynchronously.
     * The message is a byte array.
     *
     * @param channel the channel to send the message to
     * @param message the message
     * @return a {@link RedisFuture<Long>} instance
     */
    RedisFuture<Long> sendAsync(final String channel, final byte[] message);

    /**
     * Sends a message to a channel asynchronously.
     * The message is a String.
     *
     * @param channel the channel to send the message to
     * @param message the message
     * @return a {@link RedisFuture<Long>} instance
     */
    RedisFuture<Long> sendAsync(final String channel, final String message);

    /**
     * Gets the underlying Redis pub/sub connection.
     *
     * @return the Redis pub/sub connection
     */
    StatefulRedisPubSubConnection<String, byte[]> getConnection();

    /**
     * Checks if the system is connected to the server.
     *
     * @return true if the system is connected, false otherwise
     */
    boolean isConnected();
}