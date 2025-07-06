package Mesages;

import java.time.Instant;

public record UserCreationMessage(
        Long id,
        String login,
        String name,
        Integer age,
        String gender,
        String hairColor,
        Instant instant
) {}
