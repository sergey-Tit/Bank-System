package Exceptions;

/**
 * Исключение, выбрасываемое при попытке добавить в список друзей пользователя, который там уже есть.
 */
public class UserAlreadyFriendException extends RuntimeException {
    public UserAlreadyFriendException(String message) {
        super(message);
    }
}
