package com.haisy.app.auth.services;

import com.haisy.app.auth.dto.AuthRequest;
import com.haisy.app.auth.dto.AuthResponse;
import com.haisy.app.auth.dto.SignupRequest;
import com.haisy.app.user.entity.Role;
import com.haisy.app.user.entity.User;
import com.haisy.app.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginService Tests")
class LoginServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    private User activeUser;
    private AuthRequest loginRequest;

    @BeforeEach
    void setUp() {
        activeUser = new User();
        activeUser.setEmail("test@example.com");
        activeUser.setUserName("testuser");
        activeUser.setPassword("$2a$10$hashedpassword");
        activeUser.setRole(Role.USER);
        activeUser.setEnabled(true);

        loginRequest = new AuthRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("rawPassword123");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // login()
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("login: returns success with token when credentials are valid")
    void login_validCredentials_returnsSuccess() {
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("rawPassword123", "$2a$10$hashedpassword")).thenReturn(true);

        AuthResponse response = loginService.login(loginRequest);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Login successful");
        assertThat(response.getToken()).isNotNull();
        assertThat(response.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("login: returns failure when email does not exist")
    void login_emailNotFound_returnsFailure() {
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.empty());

        AuthResponse response = loginService.login(loginRequest);

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("Invalid email or password");
        assertThat(response.getToken()).isNull();
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    @DisplayName("login: returns failure when password is wrong")
    void login_wrongPassword_returnsFailure() {
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("rawPassword123", "$2a$10$hashedpassword")).thenReturn(false);

        AuthResponse response = loginService.login(loginRequest);

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("Invalid email or password");
        assertThat(response.getToken()).isNull();
    }

    @Test
    @DisplayName("login: returns failure when user account is disabled")
    void login_disabledUser_returnsFailure() {
        activeUser.setEnabled(false);
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(activeUser));

        AuthResponse response = loginService.login(loginRequest);

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("User is disabled");
        assertThat(response.getToken()).isNull();
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    @DisplayName("login: returns failure message when an unexpected exception occurs")
    void login_unexpectedException_returnsFailure() {
        when(userService.findByEmail("test@example.com")).thenThrow(new RuntimeException("DB connection error"));

        AuthResponse response = loginService.login(loginRequest);

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).contains("Login failed");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // signup()
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("signup: returns success with token for a new user")
    void signup_newUser_returnsSuccess() {
        SignupRequest request = new SignupRequest();
        request.setEmail("new@example.com");
        request.setPassword("newpass123");
        request.setUserName("newuser");

        User savedUser = new User();
        savedUser.setEmail("new@example.com");
        savedUser.setUserName("newuser");
        savedUser.setPassword("$2a$10$hashedNewPassword");
        savedUser.setRole(Role.USER);
        savedUser.setEnabled(true);

        when(userService.register(request)).thenReturn(savedUser);

        AuthResponse response = loginService.signup(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Signup successful");
        assertThat(response.getToken()).isNotNull();
        assertThat(response.getUsername()).isEqualTo("newuser");
    }

    @Test
    @DisplayName("signup: returns failure when email is already registered")
    void signup_duplicateEmail_returnsFailure() {
        SignupRequest request = new SignupRequest();
        request.setEmail("existing@example.com");
        request.setPassword("pass123");
        request.setUserName("someone");

        when(userService.register(request)).thenThrow(new RuntimeException("Email already registered"));

        AuthResponse response = loginService.signup(request);

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo("Email already registered");
        assertThat(response.getToken()).isNull();
    }

    @Test
    @DisplayName("login: returned token on success is a valid 3-part JWT")
    void login_success_tokenIsValidJwtFormat() {
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("rawPassword123", "$2a$10$hashedpassword")).thenReturn(true);

        AuthResponse response = loginService.login(loginRequest);

        assertThat(response.getToken()).isNotNull();
        assertThat(response.getToken().split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("login: ADMIN role user receives a token on successful login")
    void login_adminUser_returnsTokenWithAdminRole() {
        activeUser.setRole(Role.ADMIN);
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("rawPassword123", "$2a$10$hashedpassword")).thenReturn(true);

        AuthResponse response = loginService.login(loginRequest);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getToken()).isNotNull();
        assertThat(response.getToken().split("\\.")).hasSize(3);
    }
}
