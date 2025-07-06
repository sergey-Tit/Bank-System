package Repositories;

import Models.UserFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, Long> {
    boolean existsByUserLoginAndFriendLogin(String login, String friendLogin);
    void deleteByUserLoginAndFriendLogin(String login, String friendLogin);
    List<UserFriend> findAllByUserLogin(String login);
}
