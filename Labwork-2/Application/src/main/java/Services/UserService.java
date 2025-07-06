package Services;

import Exceptions.LoginAlreadyExistException;
import Exceptions.UserAlreadyFriendException;
import Exceptions.UserNotFoundException;
import Exceptions.UserNotFriendException;
import Models.HairColour;
import Models.User;
import Models.UserFriend;
import Repositories.UserFriendRepository;
import Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с пользователями.
 * Выполняет операции: создание пользователя, вход в систему, добавление и удаление друзей.
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserFriendRepository userFriendRepository;
    private User currentUser = null;

    /**
     * Конструктор сервиса пользователей.
     *
     * @param userRepository Репозиторий пользователей ({@link UserRepository}).
     */
    @Autowired
    public UserService(UserRepository userRepository, UserFriendRepository userFriendRepository) {
        this.userRepository = userRepository;
        this.userFriendRepository = userFriendRepository;
    }

    /**
     * Создание нового пользователя.
     *
     * @param login Логин пользователя.
     * @param name Имя пользователя.
     * @param age Возраст пользователя.
     * @param gender Пол пользователя.
     * @param hairColour Цвет волос пользователя.
     * @return {@code Long} ID созданного {@code User}
     * @throws LoginAlreadyExistException Если у существующего {@code User} есть такой логин.
     */
    @Transactional
    public User CreateUser(String login, String name, int age, String gender, HairColour hairColour)
            throws LoginAlreadyExistException {
        if (userRepository.existsByLogin(login)) {
            throw new LoginAlreadyExistException("Login already exists.");
        }

        User user = new User(login, name, age, gender, hairColour);
        currentUser = userRepository.save(user);

        return currentUser;
    }

    /**
     * Вход пользователя по логину.
     *
     * @param login Логин пользователя.
     */
    public void LoginUser(String login) throws UserNotFoundException {
        if (!userRepository.existsByLogin(login)) {
            throw new UserNotFoundException("User not found.");
        }
        currentUser = userRepository.findByLogin(login).orElseThrow();
    }

    /**
     * Возвращает информацию о текущем пользователе.
     *
     * @return Строковое представление информации о текущем пользователе.
     */
    public String GetUserInfo() {
        return currentUser.toString();
    }

    /**
     * Добавляет пользователя в список друзей.
     *
     * @param login Логин пользователя, которого нужно добавить в друзья.
     * @throws UserNotFoundException Если пользователь с таким логином не найден.
     * @throws UserAlreadyFriendException Если пользователь уже является другом.
     */
    @Transactional
    public void AddFriend(String login) throws UserNotFoundException, UserAlreadyFriendException {
        if (!userRepository.existsByLogin(login)) {
            throw new UserNotFoundException("This user does not exist.");
        }

        if (userFriendRepository.existsByUserLoginAndFriendLogin(currentUser.getLogin(), login)) {
            throw new UserAlreadyFriendException("This user is already in your friend-list.");
        }

        User friend = userRepository.findByLogin(login).orElseThrow();
        UserFriend userFriend = new UserFriend(currentUser, friend);
        userFriendRepository.save(userFriend);
    }

    /**
     * Удаляет пользователя из списка друзей.
     *
     * @param login Логин пользователя, которого нужно удалить из друзей.
     * @throws UserNotFoundException Если пользователь с таким логином не найден.
     * @throws UserNotFriendException Если пользователь не является другом.
     */
    @Transactional
    public void RemoveFriend(String login) throws UserNotFoundException, UserNotFriendException {
        System.out.println("Removing friend");
        if (!userRepository.existsByLogin(login)) {
            throw new UserNotFoundException("This user does not exist.");
        }

        if (!userFriendRepository.existsByUserLoginAndFriendLogin(currentUser.getLogin(), login)) {
            throw new UserNotFriendException("This user is not in your friend-list.");
        }

        userFriendRepository.deleteByUserLoginAndFriendLogin(currentUser.getLogin(), login);
    }

    /**
     * Возвращает всех друзей конкретного пользователя по его логину.
     *
     * @param login Логин пользователя.
     * @return {@code List<UserFriend>} Лист друзей данного пользователя.
     */
    public List<User> getFriendsByLogin(String login) throws UserNotFoundException {
        if (!userRepository.existsByLogin(login)) {
            throw new UserNotFoundException("User not found.");
        }

        List<UserFriend> userFriendsList = userFriendRepository.findAllByUserLogin(login);
        List<User> friendsList = new ArrayList<>();
        for (UserFriend userFriend : userFriendsList) {
            friendsList.add(userFriend.getFriend());
        }

        return friendsList;
    }

    /**
     * Возвращает пользователей с данным цветом волос.
     *
     * @param hairColour Цвет волос для выборки.
     * @return {@code List<User>} Лист пользователей с данным цветом волос.
     */
    public List<User> GetAllUsersByHairColour(HairColour hairColour) {
        return userRepository.findAllByHairColour(hairColour);
    }

    /**
     * Возвращает пользователей данного пола.
     *
     * @param gender Пол для выборки.
     * @return {@code List<User>} Лист пользователей данного пола.
     */
    public List<User> GetAllUsersByGender(String gender) {
        return userRepository.findAllByGender(gender);
    }

    public String GetCurrentUserLogin() {
        return currentUser.getLogin();
    }
}
