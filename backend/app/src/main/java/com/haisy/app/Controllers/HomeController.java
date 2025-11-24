package com.haisy.app.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.haisy.app.DTO.QuizRequestDTO;
import com.haisy.app.Model.Quiz;
import com.haisy.app.Services.QuizService;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class HomeController {    
    @Autowired
    QuizService quizService;

    @PostMapping("/createQuiz")
    public ResponseEntity<Map<String,String>> createQuiz(@RequestBody QuizRequestDTO quiz){
        return quizService.add(quiz);
    }

    @GetMapping("/getAllQuizzes")
    public List<Quiz> getQuizzes(){
        return quizService.getAllQuizzes();
    }




}
