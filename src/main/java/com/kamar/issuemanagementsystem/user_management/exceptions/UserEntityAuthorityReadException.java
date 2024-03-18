package com.kamar.issuemanagementsystem.user_management.exceptions;

/**
 * UserEntityAuthorityReadException represents an exception that is thrown when there is a error in reading the authority of a user entity.
 * It is a subclass of the RuntimeException class.
 * <p>
 * This exception may occur in scenarios where there is a problem retrieving or accessing the authority information of a user entity,
 * such as when there is an issue with the underlying data source or when the authority data is not properly formatted or available.
 * <p>
 * This exception provides two constructors to initialize the exception with a specified error message and an optional cause.
 * <p>
 * Example usage:
 * <pre>{@code
 * try {
 *     // Code that may throw UserEntityAuthorityReadException
 * } catch (UserEntityAuthorityReadException e) {
 *     // Error handling code
 * }
 * }</pre>
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public class UserEntityAuthorityReadException extends RuntimeException{

    public UserEntityAuthorityReadException(String message) {
        super(message);
    }

    public UserEntityAuthorityReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
