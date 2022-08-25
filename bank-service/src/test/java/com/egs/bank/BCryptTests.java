package com.egs.bank;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BCryptTests {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Test
    void passwordEncrypt(){
        String password = "faridrad";
        String encodedPassword = passwordEncoder.encode(password);
        assertTrue(passwordEncoder.matches(password, encodedPassword));
    }
}
