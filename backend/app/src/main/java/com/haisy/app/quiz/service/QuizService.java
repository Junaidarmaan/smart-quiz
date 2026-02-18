package com.haisy.app.quiz.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.haisy.app.common.dto.ApiResponse;
import com.haisy.app.quiz.dto.IsCorrectRequest;
import com.haisy.app.quiz.dto.QuizRequestDTO;
import com.haisy.app.quiz.mapper.QuizDtoMapper;
import com.haisy.app.quiz.model.Quiz;
import com.haisy.app.quiz.repository.QuestionsRepository;
import com.haisy.app.quiz.repository.QuizRepo;

@Service
public class QuizService {

    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    private final QuizRepo quizRepo;
    private final QuizDtoMapper mapper;
    private final QuestionsRepository questionRepo;

    public QuizService(QuizRepo quizRepo, QuizDtoMapper mapper, QuestionsRepository questionRepo) {
        this.quizRepo = quizRepo;
        this.mapper = mapper;
        this.questionRepo = questionRepo;
    }

    public ResponseEntity<ApiResponse<Void>> add(QuizRequestDTO dto) {
        Quiz quiz = mapper.toQuizEntity(dto);
        String receivedCode = quiz.getJoinCode();

        if (quizRepo.existsByJoinCode(receivedCode)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.fail("Join code already in use, please choose another"));
        }

        LocalDateTime today = LocalDateTime.now(IST);
        if (quiz.getSchedule().getDateTime().isBefore(today)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail("Quiz date and time must be in the future"));
        }

        quiz.getQuestions().forEach(q -> q.setQuiz(quiz));
        quizRepo.save(quiz);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Quiz created successfully"));
    }

    public ResponseEntity<ApiResponse<List<Quiz>>> getAllQuizzes() {
        List<Quiz> quizzes = quizRepo.findAll();
        return ResponseEntity.ok(ApiResponse.ok("Quizzes retrieved successfully", quizzes));
    }

    public ResponseEntity<ApiResponse<List<Quiz>>> getUpcomingQuizzes() {
        LocalDateTime today = LocalDateTime.now(IST);
        List<Quiz> upcomingQuizzes = quizRepo.findByScheduleDateTimeAfter(today);
        return ResponseEntity.ok(ApiResponse.ok("Upcoming quizzes retrieved successfully", upcomingQuizzes));
    }

    public boolean isCodeExist(String code) {
        return quizRepo.existsByJoinCode(code);
    }

    public ResponseEntity<ApiResponse<Quiz>> handleQuizJoin(String code) {
        if (!isCodeExist(code)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.fail("Invalid code, please check again"));
        }

        Quiz quiz = quizRepo.findByJoinCode(code);
        int duration = quiz.getSchedule().getDuration();
        LocalDateTime start = quiz.getSchedule().getDateTime();
        LocalDateTime end = start.plusMinutes(duration);
        LocalDateTime now = LocalDateTime.now(IST);

        boolean isActive = (now.isAfter(start) || now.isEqual(start)) 
                        && (now.isBefore(end) || now.isEqual(end));

        if (isActive) {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(ApiResponse.ok("Quiz is active, you can join", quiz));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.fail("Quiz has not started yet or is already over"));
    }

    public boolean isCorrect(IsCorrectRequest request) {
        return questionRepo
                .findByIdAndQuizQuizId(request.getQuestionId(), request.getQuizId())
                .map(q -> q.getCorrectOption().equals(request.getSelectedOption()))
                .orElse(false);
    }
}