package Exceptions;

/**
 * Исключение, выбрасываемое при попытке снять сумму, превышающую баланс счета.
 */
public class NotEnoughBalanceException extends RuntimeException {
    public NotEnoughBalanceException(String message) {
        super(message);
    }
}
