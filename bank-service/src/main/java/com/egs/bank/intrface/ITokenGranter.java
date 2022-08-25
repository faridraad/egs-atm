package com.egs.bank.intrface;

import java.time.LocalDateTime;

public interface ITokenGranter {
    String generateToken(String value);
    LocalDateTime getExpirationDateFromToken(String token);
    String getUserIdFromToken(String token);
}
