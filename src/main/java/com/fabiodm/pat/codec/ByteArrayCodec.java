package com.fabiodm.pat.codec;

import io.lettuce.core.codec.RedisCodec;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * This class implements the RedisCodec interface for encoding and decoding keys and values to and from ByteBuffers.
 * The keys are of type String, and the values are of type byte array.
 */
public final class ByteArrayCodec implements RedisCodec<String, byte[]> {

    // The charset used for encoding and decoding keys.
    private final Charset charset = StandardCharsets.UTF_8;

    /**
     * Decodes the key from the given ByteBuffer into a String.
     *
     * @param byteBuffer the ByteBuffer to decode the key from
     * @return the decoded key as a String
     */
    @Override
    public String decodeKey(final ByteBuffer byteBuffer) {
        return this.charset.decode(byteBuffer).toString();
    }

    /**
     * Decodes the value from the given ByteBuffer into a byte array.
     *
     * @param byteBuffer the ByteBuffer to decode the value from
     * @return the decoded value as a byte array
     */
    @Override
    public byte[] decodeValue(final ByteBuffer byteBuffer) {
        final byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        return bytes;
    }

    /**
     * Encodes the key from the given String into a ByteBuffer.
     *
     * @param key the key to encode into a ByteBuffer
     * @return the encoded key as a ByteBuffer
     */
    @Override
    public ByteBuffer encodeKey(final String key) {
        return this.charset.encode(key);
    }

    /**
     * Encodes the value from the given byte array into a ByteBuffer.
     *
     * @param bytes the value to encode into a ByteBuffer
     * @return the encoded value as a ByteBuffer
     */
    @Override
    public ByteBuffer encodeValue(final byte[] bytes) {
        return ByteBuffer.wrap(bytes);
    }
}