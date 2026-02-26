package com.haisy.app.auth.controller;

import com.haisy.app.auth.dto.AuthRequest;
import com.haisy.app.auth.dto.AuthResponse;
import com.haisy.app.auth.dto.SignupRequest;
import com.haisy.app.auth.jwt.JwtUtil;
import com.haisy.app.user.entity.User;
import com.haisy.app.user.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        try {
            User user = userService.findByEmail(request.getEmail())
                    .orElse(null);

            if (user == null) {
                return new AuthResponse(false, "Invalid email or password", null, null);
            }

            if (!user.isEnabled()) {
                return new AuthResponse(false, "User is disabled", null, user.getUserName());
            }

            // ⚠️ Plain password check for template
            if (!user.getPassword().equals(request.getPassword())) {
                return new AuthResponse(false, "Invalid email or password", null, user.getUserName());
            }

            String token = JwtUtil.generateToken(
                    user.getEmail(),
                    user.getRole().name()
            );

            return new AuthResponse(true, "Login successful", token, user.getUserName());
        } catch (Exception e) {
            return new AuthResponse(false, "Login failed: " + e.getMessage(), null, null);
        }
    }

    // 📝 SIGNUP
    @PostMapping("/signup")
    public AuthResponse signup(@RequestBody SignupRequest request) {
        try {
            User user = userService.register(
                    request.getEmail(),
                    request.getUserName(),
                    request.getPassword()
            );

            String token = JwtUtil.generateToken(
                    user.getEmail(),
                    user.getRole().name()
            );

            return new AuthResponse(true, "Signup successful", token, user.getUserName());
        } catch (Exception e) {
            return new AuthResponse(false, e.getMessage(), null, null);
        }
    }
}
