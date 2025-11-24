package com.haisy.app;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.haisy.app.DTO.QuizRequestDTO;
import com.haisy.app.Model.User;
import com.haisy.app.Services.UserService;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class HomeController {
    @Autowired
    UserService userService;
    

    @PostMapping("/createQuiz")
    public ResponseEntity<String> createQuiz(@RequestBody QuizRequestDTO quiz){
        System.out.println(quiz.toString());
        return ResponseEntity.status(HttpStatus.OK).body("successfully created " + quiz.toString());
    }
        

    @PostMapping("/signup")
    public ResponseEntity<Map<String,Object>> signup(@RequestBody User user){
        return userService.signUp(user);
    }

     @PostMapping("/login")
    public ResponseEntity<String> logIn(@RequestBody User user){
        return userService.longIn(user);
    }
}
