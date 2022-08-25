package com.egs.bank.utility;

import com.egs.bank.configuration.JwtConfig;
import com.egs.bank.intrface.ITokenGranter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenGranter implements ITokenGranter {

    private final JwtConfig jwtConfig;
    private Key secretKey;

    public JwtTokenGranter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        secretKey = new SecretKeySpec(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName());
    }

    @Override
    public String generateToken(String value) {
        return doGenerateToken(new HashMap<>(), value);
    }

    @Override
    public LocalDateTime getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    @Override
    public String getUserIdFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {


        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getValiditySeconds() * 1000))
                .signWith(secretKey)
                .compact();
    }

    private  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }
}
