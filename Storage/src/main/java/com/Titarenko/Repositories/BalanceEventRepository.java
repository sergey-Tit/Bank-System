package com.Titarenko.Repositories;

import com.Titarenko.Models.BalanceModifyEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceEventRepository extends JpaRepository<BalanceModifyEvent, Long> {
}
