package com.kamar.issuemanagementsystem.client_management.exceptions;

/**
 * This exception is thrown when there is an error while reading the entity from the client.
 * It is a RuntimeException, so it does not need to be caught or declared in the method signature.
 *
 * @since 1.0
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public class ClientEntityReadException extends RuntimeException{

    public ClientEntityReadException(String message) {
        super(message);
    }

    public ClientEntityReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
