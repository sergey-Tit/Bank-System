package Exceptions;

/**
 * Исключение, выбрасываемое при попытке удалить из списка друзей пользователя, которого там нет.
 */
public class UserNotFriendException extends RuntimeException {
    public UserNotFriendException(String message) {
        super(message);
    }
}
