package Repositories;

import Models.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для управления банковскими счетами.
 * Позволяет добавлять, проверять существование, получать и обновлять счета.
 */
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findAllByOwnerLogin(String login);
}