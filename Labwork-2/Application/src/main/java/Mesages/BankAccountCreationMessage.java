package Mesages;

import java.time.Instant;

public record BankAccountCreationMessage(
        String ownerLogin,
        Instant instant
) {}
