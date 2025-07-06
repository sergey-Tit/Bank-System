package Exceptions;

/**
 * Исключение, выбрасываемое, когда пользователь пытается создать нового {@code User}, используя логин, который
 * зарегистрирован на другого User.
 */
public class LoginAlreadyExistException extends RuntimeException {
    public LoginAlreadyExistException(String message) {
        super(message);
    }
}
