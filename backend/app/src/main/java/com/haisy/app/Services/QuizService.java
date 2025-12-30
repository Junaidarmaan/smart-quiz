package com.haisy.app.Services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.haisy.app.DTO.QuizJoinResponseDto;
import com.haisy.app.DTO.QuizRequestDTO;
import com.haisy.app.Logs.FileLogger;
import com.haisy.app.Mappers.QuizDtoMapper;
import com.haisy.app.Model.Question;
import com.haisy.app.Model.Quiz;
import com.haisy.app.Model.QuizResultSet;
import com.haisy.app.Repository.*;

import jakarta.transaction.Transactional;

@Service
public class QuizService {

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private QuizDtoMapper mapper;

    @Autowired
    private QuestionsRepository questionRepo;

    public ResponseEntity<Map<String, String>> add(QuizRequestDTO dto) {

        FileLogger.info("Attempting to create new quiz");

        Quiz quiz = mapper.toQuizEntity(dto);
        Map<String, String> response = new HashMap<>();

        String receivedCode = quiz.getJoinCode();
        FileLogger.debug("Received join code: " + receivedCode);

        if (quizRepo.existsByJoinCode(receivedCode)) {
            FileLogger.error("Quiz creation failed. Duplicate join code: " + receivedCode);
            response.put("message", "entered joinCode already in use please choose another");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        quiz.getQuestions().forEach(q -> q.setQuiz(quiz));

        LocalDateTime today = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        if (quiz.getSchedule().getDateTime().isBefore(today)) {
            FileLogger.error("Quiz creation failed. Scheduled time is in the past.");
            response.put("data", "Quiz date and time must be in the future");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        QuizResultSet quizResults = new QuizResultSet();
        quiz.setQuizResults(quizResults);
        quizResults.setQuiz(quiz);

        quizRepo.save(quiz);
        FileLogger.info("Quiz created successfully with join code: " + quiz.getJoinCode());

        response.put("data", "Quiz created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public List<Quiz> getAllQuizzes() {
        FileLogger.info("Fetching all quizzes");
        return quizRepo.findAll();
    }

    public ResponseEntity<Map<String, Object>> getUpcomingQuizzes() {
        LocalDateTime today = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        List<Quiz> upcomingQuizzes = quizRepo.findByScheduleDateTimeAfter(today);

        FileLogger.info("Fetched upcoming quizzes count: " + upcomingQuizzes.size());

        Map<String, Object> response = new HashMap<>();
        response.put("data", upcomingQuizzes);
        response.put("message", "Upcoming quizzes retrieved successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public boolean isCodeExist(String code) {
        boolean exists = quizRepo.existsByJoinCode(code);
        FileLogger.debug("Checking if join code exists: " + code + " -> " + exists);
        return exists;
    }
    @Transactional
    public ResponseEntity<QuizJoinResponseDto> handleQuizJoin(String code) {

        FileLogger.info("User attempting to join quiz with code: " + code);

        boolean isValidCode = isCodeExist(code);
        QuizJoinResponseDto response = new QuizJoinResponseDto();
        if (!isValidCode) {
            FileLogger.error("Invalid quiz code attempted: " + code);
            response.setAction(true);
            response.setMessage("the provided code is invalid please check again");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Quiz quiz = quizRepo.findByJoinCode(code);
        
        int duration = quiz.getSchedule().getDuration();
        LocalDateTime start = quiz.getSchedule().getDateTime();
        LocalDateTime end = start.plusMinutes(duration);
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        
        boolean allowed =
        (now.isAfter(start) || now.isEqual(start)) &&
        (now.isBefore(end) || now.isEqual(end));
        
        if (allowed) {
            QuizJoinResponseDto responseDto = mapper.toQuizJoinResponseDto(quiz);
            FileLogger.info("User allowed to join quiz: " + code);
            responseDto.setAction(true);
            responseDto.setMessage("you can join the quiz");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDto);
        }

        FileLogger.info("User denied entry. Quiz not active. Code: " + code);
        response.setAction(false);
        response.setMessage("quiz is either not started yet or already over");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    public boolean isCorrect(Map<String, Object> map) {
        int questionId = Integer.parseInt(map.get("questionId").toString());
        int quizId = Integer.parseInt(map.get("quizId").toString());
        String selectedOption = map.get("selectedOption").toString();

        FileLogger.debug("Answer check initiated | quizId=" + quizId + ", questionId=" + questionId);

        Question q = questionRepo.findByIdAndQuizQuizId(questionId, quizId);
        boolean result = q.getCorrectOption().equals(selectedOption);

        FileLogger.info("Answer validation result: " + result);
        return result;
    }

    public boolean removeQuiz(String joinCode) {
        FileLogger.info("Attempting to delete quiz with join code: " + joinCode);

        Quiz quiz = quizRepo.findByJoinCode(joinCode);
        if (quiz == null) {
            FileLogger.error("Delete failed. Quiz not found: " + joinCode);
            return false;
        }

        quizRepo.deleteById(quiz.getQuizId());
        FileLogger.info("Quiz deleted successfully: " + joinCode);
        return true;
    }
}
