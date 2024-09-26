package com.john.bryce.couponsystem.security;

import com.john.bryce.couponsystem.exceptions.CouponSystemException;
import com.john.bryce.couponsystem.exceptions.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenManager {

    private final Map<UUID, Information> tokens;

    public UUID addToken(Information information) {
        if (information == null) {
            throw new NullPointerException();
        }
        deleteToken(information);
        UUID newToken = UUID.randomUUID();

        while (tokens.containsKey(newToken)) {
            newToken = UUID.randomUUID();
        }

        tokens.put(newToken, information);
        return newToken;
    }

    private void deleteToken(Information information) {
        tokens.entrySet().removeIf((token) -> token.getValue().getId() == information.getId());
    }

    public long validateToken(UUID token, ClientType clientType) throws CouponSystemException {
        if (!tokens.containsKey(token)) {
            throw new CouponSystemException(ErrorMessage.INVALID_TOKEN);
        }

        if (tokens.get(token).getClientType() != clientType) {
            throw new CouponSystemException(ErrorMessage.UNAUTHORIZED);
        }

        tokens.get(token).setExpiration(LocalDateTime.now().plusDays(1));
        return tokens.get(token).getId();

    }

    @Scheduled(timeUnit = TimeUnit.HOURS, fixedRate = 1)
    private void removeExpiredTokens() {
        tokens.entrySet().removeIf((token) -> token.getValue().getExpiration().isBefore(LocalDateTime.now()));
    }

}
