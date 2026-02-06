package com.haisy.app.auth.services;


import com.haisy.app.auth.dto.AuthRequest;
import com.haisy.app.auth.dto.AuthResponse;
import com.haisy.app.auth.dto.SignupRequest;
import com.haisy.app.auth.jwt.JwtUtil;
import com.haisy.app.user.entity.User;
import com.haisy.app.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(AuthRequest request) {
        try {
            User user = userService.findByEmail(request.getEmail()).orElse(null);

            if (user == null) return new AuthResponse(false, "Invalid email or password", null, null);
            if (!user.isEnabled()) return new AuthResponse(false, "User is disabled", null, null);
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
                return new AuthResponse(false, "Invalid email or password", null, null);

            String token = JwtUtil.generateToken(user.getEmail(), user.getRole().name());
            return new AuthResponse(true, "Login successful", token, user.getUserName());

        } catch (Exception e) {
            return new AuthResponse(false, "Login failed: " + e.getMessage(), null, null);
        }
    }

    public AuthResponse signup(SignupRequest request) {
        try {
            User user = userService.register(request);
            String token = JwtUtil.generateToken(user.getEmail(), user.getRole().name());
            return new AuthResponse(true, "Signup successful", token, user.getUserName());

        } catch (Exception e) {
            return new AuthResponse(false, e.getMessage(), null, null);
        }
    }
}