package com.Titarenko.Mappers;

import com.Titarenko.DTO.UserDTO;
import Models.User;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getLogin(),
                user.getName(),
                user.getAge(),
                user.getGender(),
                user.getHairColour()
        );
    }
}
