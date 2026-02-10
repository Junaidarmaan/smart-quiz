package com.haisy.app.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haisy.app.auth.dto.AuthRequest;
import com.haisy.app.auth.dto.AuthResponse;
import com.haisy.app.auth.dto.SignupRequest;
import com.haisy.app.auth.services.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = AuthController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LoginService loginService;

    // ─────────────────────────────────────────────────────────────────────────
    // POST /auth/login
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /auth/login: returns 200 with success=true for valid credentials")
    void login_validCredentials_returns200WithSuccess() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setEmail("user@example.com");
        request.setPassword("password123");

        AuthResponse successResponse = new AuthResponse(true, "Login successful", "jwt.token.here", "testuser");
        when(loginService.login(any(AuthRequest.class))).thenReturn(successResponse);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.token").value("jwt.token.here"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @DisplayName("POST /auth/login: returns 200 with success=false for invalid credentials")
    void login_invalidCredentials_returns200WithFailure() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setEmail("user@example.com");
        request.setPassword("wrongpassword");

        AuthResponse failResponse = new AuthResponse(false, "Invalid email or password", null, null);
        when(loginService.login(any(AuthRequest.class))).thenReturn(failResponse);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid email or password"))
                .andExpect(jsonPath("$.token").doesNotExist());
    }

    @Test
    @DisplayName("POST /auth/login: returns 200 with success=false for disabled account")
    void login_disabledAccount_returns200WithFailure() throws Exception {
        AuthRequest request = new AuthRequest();
        request.setEmail("disabled@example.com");
        request.setPassword("password");

        AuthResponse disabledResponse = new AuthResponse(false, "User is disabled", null, null);
        when(loginService.login(any(AuthRequest.class))).thenReturn(disabledResponse);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("User is disabled"));
    }

    @Test
    @DisplayName("POST /auth/login: returns 415 when Content-Type is not JSON")
    void login_nonJsonContentType_returns415() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.TEXT_PLAIN)
                .content("bad body"))
                .andExpect(status().isUnsupportedMediaType());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POST /auth/signup
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /auth/signup: returns 200 with success=true for a new user")
    void signup_newUser_returns200WithSuccess() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setEmail("newuser@example.com");
        request.setPassword("newpass123");
        request.setUserName("newuser");

        AuthResponse successResponse = new AuthResponse(true, "Signup successful", "jwt.token.here", "newuser");
        when(loginService.signup(any(SignupRequest.class))).thenReturn(successResponse);

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Signup successful"))
                .andExpect(jsonPath("$.token").value("jwt.token.here"))
                .andExpect(jsonPath("$.username").value("newuser"));
    }

    @Test
    @DisplayName("POST /auth/signup: returns 200 with success=false when email already registered")
    void signup_duplicateEmail_returns200WithFailure() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setEmail("existing@example.com");
        request.setPassword("pass123");
        request.setUserName("existing");

        AuthResponse failResponse = new AuthResponse(false, "Email already registered", null, null);
        when(loginService.signup(any(SignupRequest.class))).thenReturn(failResponse);

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Email already registered"))
                .andExpect(jsonPath("$.token").doesNotExist());
    }
}
