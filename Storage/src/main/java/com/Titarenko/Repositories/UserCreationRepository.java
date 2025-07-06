package com.Titarenko.Repositories;

import com.Titarenko.Models.UserCreationEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCreationRepository extends JpaRepository<UserCreationEvent, Long> {
}
