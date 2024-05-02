package com.fabiodm.pat;

import com.fabiodm.pat.api.event.PatEvent;
import io.lettuce.core.pubsub.RedisPubSubListener;

/**
 * This class implements the RedisPubSubListener interface for handling Redis pub/sub messages.
 * It holds a reference to a Pat object and overrides the methods of the RedisPubSubListener interface.
 */
final class PatListener implements RedisPubSubListener<String, byte[]> {

    // The Pat object to broadcast the PatEvent to.
    private final Pat pat;

    /**
     * Constructs a PatPubSubListener with the given Pat object.
     *
     * @param pat the Pat object to broadcast the PatEvent to
     */
    public PatListener(final Pat pat) {
        this.pat = pat;
    }

    /**
     * Handles a message from a Redis pub/sub channel.
     * It creates a new PatEvent with the channel and message and broadcasts it to the Pat object.
     *
     * @param channel the channel the message was published to
     * @param message the message
     */
    @Override
    public void message(final String channel, final byte[] message) {
        this.pat.broadcast(new PatEvent(channel, message));
    }

    @Override
    public void subscribed(final String channel, final long count) {
    }

    @Override
    public void unsubscribed(final String channel, final long count) {
    }

    @Override
    public void message(final String pattern, final String channel, final byte[] message) {
    }

    @Override
    public void psubscribed(final String pattern, final long count) {
    }

    @Override
    public void punsubscribed(final String pattern, final long count) {
    }
}