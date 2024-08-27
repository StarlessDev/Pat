package com.fabiodm.pat.api;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Message;

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
     * Sends a message to a channel synchronously.
     * The message is a GeneratedMessage from the Google Protocol Buffers library.
     *
     * @param channel the channel to send the message to
     * @param message the message
     */
    void send(final String channel, final Message message);

    /**
     * Sends a message to a channel asynchronously.
     * The message is a byte array.
     *
     * @param channel the channel to send the message to
     * @param message the message
     */
    void sendAsync(final String channel, final byte[] message);

    /**
     * Sends a message to a channel asynchronously.
     * The message is a String.
     *
     * @param channel the channel to send the message to
     * @param message the message
     */
    void sendAsync(final String channel, final String message);

    /**
     * Sends a message to a channel synchronously.
     * The message is a GeneratedMessage from the Google Protocol Buffers library.
     *
     * @param channel the channel to send the message to
     * @param message the message
     */
    void sendAsync(final String channel, final Message message);

    /**
     * Checks if the system is connected to the server.
     *
     * @return true if the system is connected, false otherwise
     */
    boolean isConnected();
}