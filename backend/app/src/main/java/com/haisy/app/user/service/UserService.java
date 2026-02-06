package com.haisy.app.user.service;

import com.haisy.app.auth.dto.SignupRequest;
import com.haisy.app.user.entity.Role;
import com.haisy.app.user.entity.User;
import com.haisy.app.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 🔹 Register new user
    public User register(SignupRequest request) {
        String email = request.getEmail();
        String userName = request.getUserName();
        String rawPassword = request.getPassword();

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setEmail(email);
        user.setUserName(userName);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.USER);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    // 🔹 Find user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
