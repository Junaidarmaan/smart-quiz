package com.haisy.app.DTO;

public class GoogleUserDto {
    String googleUserId;
    String email;
    String name;
    boolean emailVerified;
    String token;
    public String getGoogleUserId() {
        return googleUserId;
    }
    public void setGoogleUserId(String googleUserId) {
        this.googleUserId = googleUserId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isEmailVerified() {
        return emailVerified;
    }
    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    
}
