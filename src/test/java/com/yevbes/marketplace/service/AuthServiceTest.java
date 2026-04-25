package com.yevbes.marketplace.service;

import com.yevbes.marketplace.entity.User;
import com.yevbes.marketplace.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "secret", "test-secret-key-for-unit-tests-only-32-chars");
        ReflectionTestUtils.setField(authService, "expiration", 86400000L);
    }

    @Test
    void register_ShouldCreateUserAndReturnToken() {
        String email = "new@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword123";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String token = authService.register(email, password);

        assertThat(token).isNotNull();
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(password);
    }

    @Test
    void register_ShouldThrowExceptionWhenEmailExists() {
        String email = "exists@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> authService.register(email, "pass"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User already exists!");

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void login_ShouldReturnTokenWhenCredentialsAreValid() {
        String email = "user@example.com";
        String password = "correct_password";
        String encodedPassword = "encodedCorrectPassword";

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        String token = authService.login(email, password);

        assertThat(token).isNotNull();
        verify(passwordEncoder).matches(password, encodedPassword);
    }

    @Test
    void login_ShouldThrowExceptionWhenUserNotFound() {
        String email = "notfound@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(email, "pass"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found!");
    }

    @Test
    void login_ShouldThrowExceptionWhenPasswordIsWrong() {
        String email = "user@example.com";
        String wrongPassword = "wrong_password";
        String encodedPassword = "encodedCorrectPassword";

        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .build();

        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(wrongPassword, encodedPassword)).thenReturn(false);

        assertThatThrownBy(() -> authService.login(email, wrongPassword))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invalid password!");
    }
}