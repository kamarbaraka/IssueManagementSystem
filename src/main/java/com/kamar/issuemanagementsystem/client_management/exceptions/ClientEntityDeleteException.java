package com.kamar.issuemanagementsystem.client_management.exceptions;

/**
 * Thrown to indicate that an exception occurred during the deletion of a client entity.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public class ClientEntityDeleteException extends RuntimeException{

    public ClientEntityDeleteException(String message) {
        super(message);
    }

    public ClientEntityDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
