package com.kamar.issuemanagementsystem.client_management.exceptions;

/**
 * The ClientEntityUpdateException class is a custom exception that is thrown when an error occurs while updating a client entity.
 * <p>
 * This exception extends the RuntimeException class, making it an unchecked exception.
 * </p>
 * <p>
 * The exception provides two constructors: one that takes a detail message as a parameter, and another that takes a detail
 * message and a cause as parameters.
 * </p>
 * <p>
 * The detail message parameter is a string that describes the specific error that occurred. It is saved for later retrieval
 * by the getMessage() method inherited from the Throwable class.
 * </p>
 * <p>
 * The cause parameter is a throwable object that caused this exception to be thrown. It is saved for later retrieval by the
 * getCause() method inherited from the Throwable class.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>{@code
 * try {
 *     // Code that updates client entity
 * } catch (ClientEntityUpdateException e) {
 *     System.out.println("An error occurred while updating the client entity: " + e.getMessage());
 *     e.printStackTrace();
 * }
 * }</pre>
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public class ClientEntityUpdateException extends RuntimeException{

    /**
     * Constructs a new {@code ClientEntityUpdateException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method).
     */
    public ClientEntityUpdateException(String message) {
        super(message);
    }

    public ClientEntityUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
