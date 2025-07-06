package com.Titarenko.Repositories;

import com.Titarenko.Models.FriendListModifyEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendListModifyRepository extends JpaRepository<FriendListModifyEvent, Long> {
}
