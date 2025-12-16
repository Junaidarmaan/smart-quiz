package com.haisy.app.Services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import java.util.Collections;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.haisy.app.DTO.GoogleUserDto;
import com.haisy.app.Services.WebSocket.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private static final String CLIENT_ID = "266908230365-8tol029060o3i6aj02kac64pl9s6ult1.apps.googleusercontent.com";
    @Autowired
    private JwtService jwtService;

    public GoogleUserDto verify(String idTokenString) throws Exception {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        
        if (idToken == null) {
            throw new RuntimeException("Invalid Google ID token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        String email = payload.getEmail();
        boolean emailVerified = Boolean.TRUE.equals(payload.getEmailVerified());
        String name = (String) payload.get("name");
        String googleUserId = payload.getSubject();
        System.out.println("User ID: " + googleUserId + ", Name: " + name + ", Email: " + email + ", Email Verified: "
                + emailVerified);
        GoogleUserDto userDto = new GoogleUserDto();
        String jwtToken = jwtService.generateToken(name);
        userDto.setToken(jwtToken);
        userDto.setGoogleUserId(googleUserId);
        userDto.setEmail(email);
        userDto.setName(name);
        userDto.setEmailVerified(emailVerified);
        return userDto;
    }
}
