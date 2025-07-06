package Repositories;

import Models.HairColour;
import Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для управления пользователями.
 * Позволяет добавлять, проверять существование и получать пользователей по их логину.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    boolean existsByLogin(String login);
    List<User> findAllByHairColour(HairColour hairColour);
    List<User> findAllByGender(String gender);
}