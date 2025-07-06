package com.Titarenko.Services;

import com.Titarenko.Exceptions.LoginAlreadyExistException;
import com.Titarenko.Models.Role;
import com.Titarenko.Models.User;
import com.Titarenko.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public void saveUser(String login, String password, Role role) throws LoginAlreadyExistException {
        User savedUser = userRepository.findByLogin(login);
        if (savedUser != null) {
            throw new LoginAlreadyExistException("Login already exists.");
        }

        User user = new User(login, new BCryptPasswordEncoder().encode(password), role);
        userRepository.save(user);
    }
}
