package com.haisy.app;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.haisy.app.Model.Quiz;
import com.haisy.app.Model.User;
import com.haisy.app.Services.QuizService;
import com.haisy.app.Services.UserService;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class HomeController {
    @Autowired
    QuizService quizService;    
    @Autowired
    UserService userService;
    @PostMapping("/create-quiz")
    public String createQuiz(@RequestBody Quiz data) throws JsonProcessingException{
        System.out.println(data.toString());
        return quizService.add(data);
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
