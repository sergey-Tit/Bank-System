package Exceptions;

/**
 * Исключение, выбрасываемое при попытке найти несуществующего пользователя.
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}
