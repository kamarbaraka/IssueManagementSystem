package com.kamar.issuemanagementsystem.user_management.exceptions;

/**
 * Represents an exception that is thrown when an error occurs while updating a user's entity role.
 *
 * This exception extends the RuntimeException class, which makes it an unchecked exception. Unchecked exceptions do not need to be declared in a method's throws clause.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public class UserEntityRoleUpdateException extends RuntimeException{

    /**
     * Represents an exception that is thrown when an error occurs while updating a user's entity role.
     */
    public UserEntityRoleUpdateException(String message) {
        super(message);
    }

    /**
     * Represents an exception that is thrown when an error occurs while updating a user's entity role.
     *
     * This exception extends the RuntimeException class, which makes it an unchecked exception. Unchecked exceptions do not need to be declared in a method's throws clause.
     *
     * @param message the detail message for the exception
     * @param cause the cause of the exception
     */
    public UserEntityRoleUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
