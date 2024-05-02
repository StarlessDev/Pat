package com.fabiodm.pat.exception;

/**
 * This class represents a custom exception that is thrown when there is an error during the registration process in Pat.
 * It extends the RuntimeException class, meaning it is an unchecked exception.
 */
public final class PatRegistrationException extends RuntimeException {

    /**
     * Constructor for the PatRegistrationException class.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
     */
    public PatRegistrationException(final String message) {
        super(message);
    }
}