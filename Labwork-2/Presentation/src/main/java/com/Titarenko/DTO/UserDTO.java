package com.Titarenko.DTO;

import Models.HairColour;

public record UserDTO(Long id, String login, String name, Integer age, String gender, HairColour hairColour) {}
