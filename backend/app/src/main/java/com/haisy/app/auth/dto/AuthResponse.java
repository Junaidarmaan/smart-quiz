package com.haisy.app.auth.dto;

public class AuthResponse {

    private boolean success;
    private String message;
    private String token;
    private String username;

    public AuthResponse(boolean success, String message, String token, String username) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.username = username;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
