package Repositories;

import Models.BankAccount;
import Models.Operation;
import Models.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findAllByHomeAccountOrTargetAccount(BankAccount homeAccount, BankAccount targetAccount);
    List<Operation> findAllByType(OperationType type);
}