package com.haisy.app.user.service;

import com.haisy.app.auth.dto.SignupRequest;
import com.haisy.app.user.entity.Role;
import com.haisy.app.user.entity.User;
import com.haisy.app.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest();
        signupRequest.setEmail("user@example.com");
        signupRequest.setPassword("rawPassword");
        signupRequest.setUserName("testuser");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // register()
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("register: saves user with BCrypt-hashed password, not plaintext")
    void register_newUser_savesWithHashedPassword() {
        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(passwordEncoder.encode("rawPassword")).thenReturn("$2a$10$hashed");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        User savedUser = new User();
        savedUser.setEmail("user@example.com");
        savedUser.setUserName("testuser");
        savedUser.setPassword("$2a$10$hashed");
        savedUser.setRole(Role.USER);
        savedUser.setEnabled(true);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.register(signupRequest);

        verify(userRepository).save(userCaptor.capture());
        User captured = userCaptor.getValue();

        assertThat(captured.getPassword()).isEqualTo("$2a$10$hashed");
        assertThat(captured.getPassword()).isNotEqualTo("rawPassword");
        assertThat(captured.getEmail()).isEqualTo("user@example.com");
        assertThat(captured.getUserName()).isEqualTo("testuser");
        assertThat(captured.getRole()).isEqualTo(Role.USER);
        assertThat(captured.isEnabled()).isTrue();
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("register: throws RuntimeException when email is already registered")
    void register_duplicateEmail_throwsException() {
        when(userRepository.existsByEmail("user@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(signupRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email already registered");

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("register: assigns default role USER to every new registration")
    void register_newUser_assignsDefaultRoleUser() {
        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hashed");

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.register(signupRequest);

        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("register: calls passwordEncoder.encode() with the raw password")
    void register_callsEncoderWithRawPassword() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode("rawPassword")).thenReturn("$2a$10$hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.register(signupRequest);

        verify(passwordEncoder).encode("rawPassword");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // findByEmail()
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findByEmail: returns user when email exists")
    void findByEmail_existingEmail_returnsUser() {
        User user = new User();
        user.setEmail("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail("user@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("user@example.com");
    }

    @Test
    @DisplayName("findByEmail: returns empty Optional when email does not exist")
    void findByEmail_nonExistentEmail_returnsEmpty() {
        when(userRepository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail("ghost@example.com");

        assertThat(result).isEmpty();
    }
}
