package com.haisy.app.quiz.controller;

import com.haisy.app.common.dto.ApiResponse;
import com.haisy.app.quiz.dto.IsCorrectRequest;
import com.haisy.app.quiz.dto.QuizRequestDTO;
import com.haisy.app.quiz.model.Quiz;
import com.haisy.app.quiz.service.QuizService;
import com.haisy.app.services.websocket.LeaderBoards;
import com.haisy.app.services.websocket.UserProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;
    private final LeaderBoards leaderBoards;

    public QuizController(QuizService quizService, LeaderBoards leaderBoards) {
        this.quizService = quizService;
        this.leaderBoards = leaderBoards;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> createQuiz(@RequestBody QuizRequestDTO quiz) {
        return quizService.add(quiz);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Quiz>>> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<Quiz>>> getUpcomingQuizzes() {
        return quizService.getUpcomingQuizzes();
    }

    @PostMapping("/join/{code}")
    public ResponseEntity<ApiResponse<Quiz>> joinQuiz(@PathVariable String code) {
        return quizService.handleQuizJoin(code);
    }

    @PostMapping("/isCorrect")
    public boolean isCorrect(@RequestBody IsCorrectRequest request) {
        return quizService.isCorrect(request);
    }

    @PostMapping("/currentQuestion")
    public UserProfile getCurrentQuestion(@RequestBody UserProfile userProfile) {
        return leaderBoards.getUserProfileStatus(userProfile);
    }

    @PostMapping("/isValid/{id}")
    public boolean isValidQuiz(@PathVariable String id) {
        return leaderBoards.isValidQuizId(id);
    }
}