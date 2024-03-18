package com.kamar.issuemanagementsystem.user_management.exceptions;

/**
 * The UserEntityRoleDeleteException class is a custom exception that is thrown when an error occurs
 * during the deletion of a user's entity role.
 *
 * <p>Instances of this exception are typically thrown in the event of a failed deletion operation,
 * indicating that the specified user's entity role could not be deleted due to some error.</p>
 *
 * <p>This exception extends the RuntimeException class, making it an unchecked exception. It includes
 * two constructors for creating instances with a custom error message and one constructor for creating
 * instances with a custom error message and a cause.</p>
 *
 * <p>Example usage:</p>
 *
 * <pre>{@code
 * try {
 *    // perform user's entity role deletion operation
 * } catch (UserEntityRoleDeleteException e) {
 *    // handle exception
 *    System.out.println(e.getMessage());
 * }
 * }</pre>
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public class UserEntityRoleDeleteException extends RuntimeException{

    public UserEntityRoleDeleteException(String message) {
        super(message);
    }

    public UserEntityRoleDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
