package com.fabiodm.pat.exception;

/**
 * This class represents a custom exception that is thrown when there is an error invoking a PatEvent.
 * It extends the RuntimeException class, meaning it is an unchecked exception.
 */
public final class PatEventInvocationException extends RuntimeException {

    /**
     * Constructor for the PatEventInvocationException class.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public PatEventInvocationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}