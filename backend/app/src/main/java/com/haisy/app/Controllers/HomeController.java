package com.haisy.app.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.haisy.app.DTO.GoogleUserDto;
import com.haisy.app.DTO.QuizJoinResponseDto;
import com.haisy.app.DTO.QuizRequestDTO;
import com.haisy.app.Model.Quiz;
import com.haisy.app.Services.GeminiService;
import com.haisy.app.Services.LoginService;
import com.haisy.app.Services.QuizService;
import com.haisy.app.Services.WebSocket.LeaderBoards;
import com.haisy.app.Services.WebSocket.UserProfile;
import com.haisy.app.Logs.FileLogger;

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

    @Autowired
    LoginService loginService;

    @PostMapping("/isValid/{id}")
    public boolean isValidQuiz(@PathVariable String id) {
        FileLogger.info("Checking validity of quiz id: " + id);
        return lb.isValidQuizId(id);
    }

    @PostMapping("/createQuiz")
    public ResponseEntity<Map<String, String>> createQuiz(@RequestBody QuizRequestDTO quiz) {
        FileLogger.info("Creating new quiz");
        return quizService.add(quiz);
    }

    @GetMapping("/getAllQuizzes")
    public List<Quiz> getQuizzes() {
        FileLogger.info("Fetching all quizzes");
        return quizService.getAllQuizzes();
    }

    @GetMapping("/getUpcomingQuizzes")
    public ResponseEntity<Map<String, Object>> getUpcomingQuizzes() {
        FileLogger.info("Fetching upcoming quizzes");
        return quizService.getUpcomingQuizzes();
    }

    @PostMapping("/joinQuiz/{code}")
    public ResponseEntity<QuizJoinResponseDto> handleJoin(@PathVariable String code) {
        FileLogger.info("User attempting to join quiz with code: " + code);
        return quizService.handleQuizJoin(code);
    }

    @PostMapping("/gemini")
    public String generateQuestions(@RequestBody Map<String, Object> request) {
        FileLogger.info("Generating quiz questions via Gemini");
        String topic = request.get("topic").toString();
        int quantity = Integer.parseInt(request.get("quantity").toString());
        int difficulty = Integer.parseInt(request.get("difficulty").toString());
        return gemini.generateQuestions(topic, quantity, difficulty);
    }

    @PostMapping("/isCorrect")
    public boolean isCorrect(@RequestBody Map<String, Object> request) {
        FileLogger.info("Validating answer");
        return quizService.isCorrect(request);
    }

    @PostMapping("/getCurrentQuestion")
    public UserProfile getProfileStatus(@RequestBody UserProfile userProfile) {
        FileLogger.info("Fetching current user progress for: " + userProfile.getUserName());
        return lb.getUserProfileStatus(userProfile);
    }

    @PostMapping("/verifyToken")
    public ResponseEntity<Map<String, Object>> verifyToken(@RequestBody Map<String, String> request) {
        FileLogger.info("Verifying Google login token");
        try {
            GoogleUserDto response = loginService.verify(request.get("idToken"));
            return ResponseEntity.ok(Map.of("status", "success", "data", response));
        } catch (Exception e) {
            FileLogger.error("Token verification failed: " + e.getMessage());
            return ResponseEntity.status(401).body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
}
