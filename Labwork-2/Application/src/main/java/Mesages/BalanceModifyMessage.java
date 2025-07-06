package Mesages;

import java.math.BigDecimal;
import java.time.Instant;

public record BalanceModifyMessage(
        String operation,
        BigDecimal amount,
        Instant instant
) {}
