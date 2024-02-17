package com.kamar.issuemanagementsystem.user_management.exceptions;

/**
 * UserRegistrationException is a custom exception class that is thrown when an error occurs during user registration.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public class UserRegistrationException extends RuntimeException{

    /**
     * Constructs a new UserRegistrationException with the specified detail message.
     *
     * @param message the detail message describing the exception
     */
    public UserRegistrationException(String message) {
        super(message);
    }

    /**
     * UserRegistrationException is a custom exception class that is thrown when an error occurs during user registration.
     */
    public UserRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
