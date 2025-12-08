package com.haisy.app.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.haisy.app.DTO.QuizRequestDTO;
import com.haisy.app.Model.Quiz;
import com.haisy.app.Services.GeminiService;
import com.haisy.app.Services.QuizService;
import com.haisy.app.Services.WebSocket.LeaderBoards;
import com.haisy.app.Services.WebSocket.UserProfile;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class HomeController {    
    @Autowired
    QuizService quizService;

    @Autowired
    GeminiService gemini;

    @Autowired
    LeaderBoards lb;

    @PostMapping("/isValid/{id}")
    public boolean isValidQuiz(@PathVariable String id ){
        return lb.isValidQuizId(id);
    }
    
    @PostMapping("/createQuiz")
    public ResponseEntity<Map<String,String>> createQuiz(@RequestBody QuizRequestDTO quiz){
        return quizService.add(quiz);
    }

    @GetMapping("/getAllQuizzes")
    public List<Quiz> getQuizzes(){
        return quizService.getAllQuizzes();
    }

    @GetMapping("/getUpcomingQuizzes")
    public ResponseEntity<Map<String,Object>> getUpcomingQuizzes(){
        return quizService.getUpcomingQuizzes();
    }

    @PostMapping("/joinQuiz/{code}")
    public ResponseEntity<Map<String,Object>> handleJoin(@PathVariable String code){
        return quizService.handleQuizJoin(code);
    }

    @PostMapping("/gemini")
    public String generateQuestions(@RequestBody Map<String,Object> obj){
        String topic = obj.get("topic").toString();
        int quantity = Integer.parseInt(obj.get("quantity").toString());
        int difficulty = Integer.parseInt(obj.get("difficulty").toString());
        
        return gemini.generateQuestions(topic,quantity,difficulty);


    } 
    @PostMapping("/isCorrect")
    public boolean isCorrect(@RequestBody Map<String,Object> request){
        return quizService.isCorrect(request);
    }
    





}
