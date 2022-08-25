package com.egs.bank;

import com.egs.bank.utility.JwtTokenGranter;
import com.egs.bank.configuration.JwtConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class JwtTokenGranterUnitTest {
    private JwtTokenGranter tokenGranter;

    @BeforeEach
    void setup() {
        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.setSecret("EG$6YGh&2iu$%gbhP");
        jwtConfig.setValiditySeconds(100);
        tokenGranter = new JwtTokenGranter(jwtConfig);
    }
    @Test
    void generateToken(){
        String token = tokenGranter.generateToken("userId");
        assertNotNull(token);
    }

    @Test
    void getUserId(){
        String userId = "userId";
        String token = tokenGranter.generateToken("userId");
        String userIdFromToken = tokenGranter.getUserIdFromToken(token);
        assertEquals(userId, userIdFromToken);
    }

    @Test
    void checkExpire(){
        String token = tokenGranter.generateToken("userId");
        LocalDateTime expirationDate = tokenGranter.getExpirationDateFromToken(token);
        assertEquals(expirationDate.truncatedTo(ChronoUnit.SECONDS), LocalDateTime.now().plusMinutes(10).truncatedTo(ChronoUnit.SECONDS));
    }
}
