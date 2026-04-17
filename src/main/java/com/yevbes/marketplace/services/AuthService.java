package com.yevbes.marketplace.services;

import com.yevbes.marketplace.entity.User;
import com.yevbes.marketplace.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

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
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User already exists!");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        userRepository.save(user);

        return generateToken(email);
    }

    public String login(String email, String password) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password!");
        }

        return generateToken(email);
    }
}