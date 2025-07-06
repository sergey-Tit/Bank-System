import Exceptions.NotEnoughBalanceException;
import Models.BankAccount;
import Models.HairColour;
import Models.User;
import Repositories.BankAccountRepository;
import Repositories.OperationRepository;
import Services.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OperationTests {
    @Mock
    private BankAccountRepository bankAccountRepositoryMock;

    @Mock
    private OperationRepository operationRepositoryMock;

    @InjectMocks
    private BankAccountService bankAccountService;

    private final long id = 1;
    private final String login = "login";
    private final String name = "name";
    private final int age = 20;
    private final String gender = "M";
    private final HairColour hairColour = HairColour.BLACK;
    private final BigDecimal startValue = new BigDecimal("1000");
    private BankAccount bankAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User user = new User(login, name, age, gender, hairColour);
        bankAccount = new BankAccount(user);
        bankAccount.setBalance(startValue);

        when(bankAccountRepositoryMock.findById(id)).thenReturn(Optional.ofNullable(bankAccount));
        when(bankAccountRepositoryMock.existsById(id)).thenReturn(true);
    }

    @Test
    void CorrectWithdrawTest() {
        BigDecimal amountToWithdraw = new BigDecimal("200");
        BigDecimal expectedAmount = new BigDecimal("800");

        bankAccountService.Withdraw(id, amountToWithdraw);

        assertEquals(expectedAmount, bankAccountService.GetBalance(id));
        verify(bankAccountRepositoryMock, times(1)).save(bankAccount);
    }

    @Test
    void IncorrectWithdrawTest() {
        BigDecimal amountToWithdraw = new BigDecimal("2000");

        Throwable exception = assertThrows(NotEnoughBalanceException.class,
                () -> bankAccountService.Withdraw(id, amountToWithdraw));

        assertEquals("Not enough balance.", exception.getMessage());
    }

    @Test
    void DepositTest() {
        BigDecimal amountToDeposit = new BigDecimal("200");
        BigDecimal expectedAmount = new BigDecimal("1200");

        bankAccountService.Deposit(id, amountToDeposit);

        assertEquals(expectedAmount, bankAccountService.GetBalance(id));
        verify(bankAccountRepositoryMock, times(1)).save(bankAccount);
    }
}