package com.haisy.app.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.haisy.app.Model.User;
import com.haisy.app.Services.UserService;

@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String,Object>> signup(@RequestBody User user){
        return userService.signUp(user);
    }

     @PostMapping("/login")
    public ResponseEntity<String> logIn(@RequestBody User user){
        return userService.longIn(user);
    }
}
