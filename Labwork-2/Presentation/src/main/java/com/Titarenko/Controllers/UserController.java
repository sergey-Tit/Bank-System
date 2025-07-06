package com.Titarenko.Controllers;

import Exceptions.UserNotFoundException;
import Models.HairColour;
import Models.User;
import com.Titarenko.DTO.UserDTO;
import Services.UserService;
import com.Titarenko.Mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "409", description = "Such login already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestParam("login") String login,
                                                @RequestParam("name") String name,
                                                @RequestParam("age") int age,
                                                @RequestParam("gender") String gender,
                                                @RequestParam("hair_colour") HairColour hairColour) {
        User user = userService.CreateUser(login, name, age, gender, hairColour);

        return new ResponseEntity<>(UserMapper.toDTO(user), HttpStatus.CREATED);
    }

    @Operation(summary = "Get user info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Info provided"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{login}")
    public ResponseEntity<String> getUserInfo(@PathVariable("login") String login) throws UserNotFoundException {
        userService.LoginUser(login);
        String info = userService.GetUserInfo();

        return ResponseEntity.ok(info);
    }

    @Operation(summary = "Add new friend")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend added"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "User already in friend-list")
    })
    @PostMapping("/{login}/add-friend/{friendLogin}")
    public ResponseEntity<String> addFriend(@PathVariable("login") String login,
                                            @PathVariable("friendLogin") String friendLogin) throws UserNotFoundException {
        userService.LoginUser(login);
        userService.AddFriend(friendLogin);

        return ResponseEntity.ok("Friend added");
    }

    @Operation(summary = "Remove friend")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend removed"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "User not in friend-list")
    })
    @DeleteMapping("/{login}/remove-friend/{friendLogin}")
    public ResponseEntity<String> removeFriend(@PathVariable("login") String login,
                                               @PathVariable("friendLogin") String friendLogin) throws UserNotFoundException {
        userService.LoginUser(login);
        userService.RemoveFriend(friendLogin);

        return ResponseEntity.ok("Friend removed");
    }

    @Operation(summary = "Get users' friends by his login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friends provided"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{login}/friends")
    public ResponseEntity<List<UserDTO>> getFriends(@PathVariable("login") String login) throws UserNotFoundException {
        List<User> friends = userService.getFriendsByLogin(login);
        List<UserDTO> friendsDTO = new ArrayList<>();
        for (User user : friends) {
            friendsDTO.add(UserMapper.toDTO(user));
        }

        return ResponseEntity.ok(friendsDTO);
    }

    @Operation(summary = "Get users by hair colour")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users provided")
    })
    @GetMapping("/hair-colour/{hairColour}")
    public ResponseEntity<List<UserDTO>> getUsersByHairColour(@PathVariable("hairColour") HairColour hairColour) {
        List<User> users = userService.GetAllUsersByHairColour(hairColour);
        List<UserDTO> usersDTO = new ArrayList<>();
        for (User user : users) {
            usersDTO.add(UserMapper.toDTO(user));
        }

        return ResponseEntity.ok(usersDTO);
    }

    @Operation(summary = "Get users by gender")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users provided")
    })
    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<UserDTO>> getUsersByHairColour(@PathVariable("gender") String gender) {
        List<User> users = userService.GetAllUsersByGender(gender);
        List<UserDTO> usersDTO = new ArrayList<>();
        for (User user : users) {
            usersDTO.add(UserMapper.toDTO(user));
        }

        return ResponseEntity.ok(usersDTO);
    }
}
