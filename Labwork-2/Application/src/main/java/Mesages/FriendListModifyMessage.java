package Mesages;

import java.time.Instant;

public record FriendListModifyMessage(
        String operation,
        String friendLogin,
        Instant instant
) {}
