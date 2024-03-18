package com.kamar.issuemanagementsystem.user_management.exceptions;

/**
 * UserEntityAuthorityWriteException is a runtime exception that is thrown when there is an error in writing authority for a user entity.
 * It extends the RuntimeException class.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public class UserEntityAuthorityWriteException extends RuntimeException{

    public UserEntityAuthorityWriteException(String message) {
        super(message);
    }

    public UserEntityAuthorityWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
