package com.Titarenko.Controllers;

import com.Titarenko.DTO.HairColour;
import com.Titarenko.DTO.UserDTO;
import com.Titarenko.Models.Role;
import com.Titarenko.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Value("${bank.service.url}")
    private String bankServiceUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin/new_client")
    public ResponseEntity<UserDTO> createClient(@RequestParam("login") String login,
                                                @RequestParam("password") String password,
                                                @RequestParam("name") String name,
                                                @RequestParam("age") int age,
                                                @RequestParam("gender") String gender,
                                                @RequestParam("hair_colour") HairColour hairColour) {
        userService.saveUser(login, password, Role.CLIENT);

        return restTemplate.exchange(
                bankServiceUrl + "/users/register?login=" + login + "&name=" + name + "&age=" + age +
                        "&gender=" + gender + "&hair_colour=" + hairColour,
                HttpMethod.POST,
                null,
                UserDTO.class
        );
    }

    @PostMapping("/admin/new_admin")
    public void createAdmin(@RequestParam("login") String login,
                            @RequestParam("password") String password) {
        userService.saveUser(login, password, Role.ADMIN);
    }

    @GetMapping("/admin/hair-colour/{hairColour}")
    public ResponseEntity<List<UserDTO>> getUsersByHairColour(@PathVariable("hairColour") HairColour hairColour) {
        return restTemplate.exchange(
                bankServiceUrl + "/users/hair-colour/" + hairColour,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
    }

    @GetMapping("/admin/gender/{gender}")
    public ResponseEntity<List<UserDTO>> getUsersByGender(@PathVariable("gender") String gender) {
        return restTemplate.exchange(
                bankServiceUrl + "/users/gender/" + gender,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
    }

    @GetMapping("/admin/{login}")
    public ResponseEntity<String> getUserInfo(@PathVariable("login") String login) {
        return restTemplate.exchange(
                bankServiceUrl + "/users/" + login,
                HttpMethod.GET,
                null,
                String.class
        );
    }

    @GetMapping("/client/me")
    public ResponseEntity<String> getCurrentUserInfo(Authentication authentication) {
        return restTemplate.exchange(
                bankServiceUrl + "/users/" + authentication.getName(),
                HttpMethod.GET,
                null,
                String.class
        );
    }

    @PostMapping("/client/add-friend/{friendLogin}")
    public ResponseEntity<String> addFriend(@PathVariable("friendLogin") String friendLogin, Authentication authentication) {
        return restTemplate.exchange(
                bankServiceUrl + "/users/" + authentication.getName() + "/add-friend/" + friendLogin,
                HttpMethod.POST,
                null,
                String.class
        );
    }

    @DeleteMapping("/client/remove-friend/{friendLogin}")
    public ResponseEntity<String> removeFriend(@PathVariable("friendLogin") String friendLogin, Authentication authentication) {
        return restTemplate.exchange(
                bankServiceUrl + "/users/" + authentication.getName() + "/remove-friend/" + friendLogin,
                HttpMethod.DELETE,
                null,
                String.class
        );
    }
}
