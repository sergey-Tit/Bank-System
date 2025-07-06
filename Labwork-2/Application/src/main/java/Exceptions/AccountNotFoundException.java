package Exceptions;

/**
 * Исключение, выбрасываемое при попытке найти несуществующий банковский счет.
 */
public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
