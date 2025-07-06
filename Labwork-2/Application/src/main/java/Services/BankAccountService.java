package Services;

import Exceptions.AccountNotFoundException;
import Exceptions.NotEnoughBalanceException;
import Exceptions.UserNotFoundException;
import Models.BankAccount;
import Models.Operation;
import Models.OperationType;
import Models.User;
import Repositories.BankAccountRepository;
import Repositories.OperationRepository;
import Repositories.UserFriendRepository;
import Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Сервис для работы с банковскими счетами.
 * Выполняет операции: создание счета, вход в аккаунт, снятие, депозит и перевод средств.
 */
@Service
public class BankAccountService {
    private final String friendCommission = "0.03";
    private final String commission = "0.10";
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final OperationRepository operationRepository;
    private final UserFriendRepository userFriendRepository;

    /**
     * Конструктор сервиса банковских счетов.
     *
     * @param bankAccountRepository Репозиторий банковских счетов ({@link BankAccountRepository}).
     * @param userRepository Репозиторий пользователей ({@link UserRepository}).
     * @param operationRepository Репозиторий операций ({@link OperationRepository}).
     * @param userFriendRepository Репозиторий записей дружбы между пользователями ({@link UserFriendRepository}).
     */
    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository, UserRepository userRepository,
                              OperationRepository operationRepository, UserFriendRepository userFriendRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
        this.operationRepository = operationRepository;
        this.userFriendRepository = userFriendRepository;
    }

    /**
     * Создать новый банковский счет для текущего пользователя.
     *
     * @param ownerLogin Логин владельца создаваемого счета.
     * @return Идентификатор созданного счета типа {@code long}.
     */
    @Transactional
    public BankAccount CreateAccount(String ownerLogin) throws UserNotFoundException {
        if (!userRepository.existsByLogin(ownerLogin)) {
            throw new UserNotFoundException("User not found.");
        }

        User owner = userRepository.findByLogin(ownerLogin).orElseThrow();

        return bankAccountRepository.save(new BankAccount(owner));
    }

    /**
     * Возвращает текущий баланс банковского счета.
     *
     * @param id Идентификатор счета.
     * @return {@link BigDecimal} Баланс текущего счета.
     */
    public BigDecimal GetBalance(Long id) throws AccountNotFoundException {
        if (!bankAccountRepository.existsById(id)) {
            throw new AccountNotFoundException("Account not found.");
        }

        BankAccount account = bankAccountRepository.findById(id).orElseThrow();

        return account.getBalance();
    }

    /**
     * Снимает указанную сумму с текущего банковского счета.
     *
     * @param id Идентификатор счета.
     * @param amount Сумма для снятия.
     * @throws NotEnoughBalanceException Если на счете недостаточно средств.
     */
    @Transactional
    public void Withdraw(Long id, BigDecimal amount) throws NotEnoughBalanceException, AccountNotFoundException {
        if (!bankAccountRepository.existsById(id)) {
            throw new AccountNotFoundException("Account not found.");
        }
        BankAccount account = bankAccountRepository.findById(id).orElseThrow();
        if (account.getBalance().compareTo(amount) < 0) {
            throw new NotEnoughBalanceException("Not enough balance.");
        }

        account.setBalance(account.getBalance().subtract(amount));

        operationRepository.save(new Operation(
                OperationType.WITHDRAW,
                amount,
                null,
                account,
                Instant.now()
        ));

        bankAccountRepository.save(account);
    }

    /**
     * Вносит указанную сумму на текущий банковский счет.
     *
     * @param id Идентификатор счета.
     * @param amount Сумма для депозита.
     */
    @Transactional
    public void Deposit(Long id, BigDecimal amount) throws AccountNotFoundException {
        if (!bankAccountRepository.existsById(id)) {
            throw new AccountNotFoundException("Account not found.");
        }
        BankAccount account = bankAccountRepository.findById(id).orElseThrow();
        account.setBalance(account.getBalance().add(amount));

        operationRepository.save(new Operation(
                OperationType.DEPOSIT,
                amount,
                null,
                account,
                Instant.now()
        ));

        bankAccountRepository.save(account);
    }

    /**
     * Переводит средства на другой банковский счет.
     *
     * @param homeAccountId Идентификатор домашнего счета, с которого производится перевод.
     * @param targetAccountId Идентификатор счета получателя.
     * @param amount Сумма для перевода.
     * @throws NotEnoughBalanceException Если на текущем счете недостаточно средств.
     * @throws AccountNotFoundException Если целевой счет или домашний счет не найден.
     */
    @Transactional
    public void Transfer(Long homeAccountId, long targetAccountId, BigDecimal amount) throws NotEnoughBalanceException,
            AccountNotFoundException {
        if (!bankAccountRepository.existsById(homeAccountId)) {
            throw new AccountNotFoundException("Account not found.");
        }
        BankAccount homeAccount = bankAccountRepository.findById(homeAccountId).orElseThrow();
        if (homeAccount.getBalance().compareTo(amount) < 0) {
            throw new NotEnoughBalanceException("Not enough balance.");
        }
        if (!bankAccountRepository.existsById(targetAccountId)) {
            throw new AccountNotFoundException("Account not found.");
        }

        User user = homeAccount.getOwner();
        BankAccount targetAccount = bankAccountRepository.findById(targetAccountId).orElseThrow();
        BigDecimal finalAmount;
        if (targetAccount.getOwner().getLogin().equals(homeAccount.getOwner().getLogin())) {
            finalAmount = amount;
        }
        else if (userFriendRepository.existsByUserLoginAndFriendLogin(user.getLogin(),
                targetAccount.getOwner().getLogin())) {
            finalAmount = amount.subtract(amount.multiply(new BigDecimal(friendCommission)));
        }
        else {
            finalAmount = amount.subtract(amount.multiply(new BigDecimal(commission)));
        }

        homeAccount.setBalance(homeAccount.getBalance().subtract(amount));
        targetAccount.setBalance(targetAccount.getBalance().add(finalAmount));

        bankAccountRepository.save(homeAccount);
        bankAccountRepository.save(targetAccount);

        operationRepository.save(new Operation(
                OperationType.TRANSFER,
                amount,
                homeAccount,
                targetAccount,
                Instant.now()
        ));
    }

    /**
     * Возвращает аккаунты конкретного пользователя по его Id.
     *
     * @param login Логин пользователя.
     * @return {@code List<BankAccount>} Лист аккаунтов данного пользователя.
     */
    public List<BankAccount> getBankAccountsByOwnerLogin(String login) throws UserNotFoundException {
        if (!userRepository.existsByLogin(login)) {
            throw new UserNotFoundException("User not found.");
        }

        return bankAccountRepository.findAllByOwnerLogin(login);
    }

    /**
     * Возвращает все банковские аккаунты в системе.
     *
     * @return {@code List<BankAccount>} Лист аккаунтов, зарегистрированных в системе.
     */
    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    /**
     * Возвращает все операции данного на вход типа.
     *
     * @param type Тип операций для выборки.
     * @return {@code List<Operation>} Лист операций данного типа.
     */
    public List<Operation> getOperationsByType(OperationType type) {
        return operationRepository.findAllByType(type);
    }

    /**
     * Возвращает все операции, произведенного с аккаунта данного ID.
     *
     * @param id Идентификатор аккаунта, по которому нужна выборка операций.
     * @return {@code List<Operation>} Лист операций, произведенных с аккаунта с данным ID.
     */
    public List<Operation> getOperationsByHomeAccountId(Long id) throws AccountNotFoundException {
        if (!bankAccountRepository.existsById(id)) {
            throw new AccountNotFoundException("Account not found.");
        }

        BankAccount account = bankAccountRepository.findById(id).orElseThrow();

        return operationRepository.findAllByHomeAccountOrTargetAccount(account, account);
    }
}
