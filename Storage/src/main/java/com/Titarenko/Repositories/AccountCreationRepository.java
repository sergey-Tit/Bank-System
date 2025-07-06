package com.Titarenko.Repositories;

import com.Titarenko.Models.BankAccountCreationEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCreationRepository extends JpaRepository<BankAccountCreationEvent, Long> {
}
