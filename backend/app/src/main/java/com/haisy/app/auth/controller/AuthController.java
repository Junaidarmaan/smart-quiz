package com.haisy.app.auth.controller;

import com.haisy.app.auth.dto.AuthRequest;
import com.haisy.app.auth.dto.AuthResponse;
import com.haisy.app.auth.dto.SignupRequest;
import com.haisy.app.auth.services.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginService loginService;

    public AuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return loginService.login(request);
    }

    @PostMapping("/signup")
    public AuthResponse signup(@RequestBody SignupRequest request) {
        return loginService.signup(request);
    }
}