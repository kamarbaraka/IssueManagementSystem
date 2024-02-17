package com.kamar.issuemanagementsystem.user_management.exceptions;

/**
 * The UserActivationException class represents an exception that is thrown when there is an error in the user activation process.
 * It extends the RuntimeException class, making it an unchecked exception.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public class UserActivationException extends RuntimeException{
    /**
     * UserActivationException represents an exception that is thrown when there is an error in user activation process.
     */
    public UserActivationException(String message) {
        super(message);
    }

    /**
     * UserActivationException represents an exception that is thrown when there is an error in the user activation process.
     */
    public UserActivationException(String message, Throwable cause) {
        super(message, cause);
    }
}
