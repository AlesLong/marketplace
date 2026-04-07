package com.yevbes.marketplace.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

@Service
public class AuthService {

    private static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    private final HashMap<String, String> users = new HashMap<>();

    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSigningKey())
                .compact();
    }

    public String register(String email, String password) {
        if (users.containsKey(email)) {
            throw new RuntimeException("User already exists!");
        }

        users.put(email, password);
        return generateToken(email);
    }

    public String login(String email, String password) {
        String storedPassword = users.get(email);

        if (storedPassword == null || !storedPassword.equals(password)){
            throw new RuntimeException("Invalid email or password!");
        }

        return generateToken(email);
    }
}
