package com.kamar.issuemanagementsystem.client_management.exceptions;

/**
 * {@code ClientEntityWriteException} is a custom exception class that extends {@link RuntimeException}.
 * It is thrown when an error occurs while writing a client entity.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public class ClientEntityWriteException extends RuntimeException{

    public ClientEntityWriteException(String message) {
        super(message);
    }

    public ClientEntityWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
