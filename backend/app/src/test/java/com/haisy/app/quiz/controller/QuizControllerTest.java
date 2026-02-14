package com.haisy.app.quiz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haisy.app.common.dto.ApiResponse;
import com.haisy.app.quiz.dto.IsCorrectRequest;
import com.haisy.app.quiz.dto.QuizRequestDTO;
import com.haisy.app.quiz.model.Quiz;
import com.haisy.app.quiz.model.Schedule;
import com.haisy.app.quiz.service.QuizService;
import com.haisy.app.websocket.services.LeaderBoards;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = QuizController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@DisplayName("QuizController Tests")
class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private QuizService quizService;

    @MockitoBean
    private LeaderBoards leaderBoards;

    // ── helpers ──────────────────────────────────────────────────────────────

    private Quiz buildQuiz(String code) {
        Schedule schedule = new Schedule();
        schedule.setDateTime(LocalDateTime.now().plusDays(1));
        schedule.setDuration(30);

        Quiz quiz = new Quiz();
        quiz.setJoinCode(code);
        quiz.setSchedule(schedule);
        quiz.setQuestions(List.of());
        return quiz;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POST /quiz/create
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /quiz/create: returns 201 when quiz is created successfully")
    void createQuiz_success_returns201() throws Exception {
        when(quizService.add(any(QuizRequestDTO.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.ok("Quiz created successfully")));

        QuizRequestDTO dto = new QuizRequestDTO();
        dto.setJoinCode("TEST01");

        mockMvc.perform(post("/quiz/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Quiz created successfully"));
    }

    @Test
    @DisplayName("POST /quiz/create: returns 409 when join code is duplicate")
    void createQuiz_duplicateCode_returns409() throws Exception {
        when(quizService.add(any(QuizRequestDTO.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.fail("Join code already in use, please choose another")));

        QuizRequestDTO dto = new QuizRequestDTO();
        dto.setJoinCode("DUPE01");

        mockMvc.perform(post("/quiz/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Join code already in use, please choose another"));
    }

    @Test
    @DisplayName("POST /quiz/create: returns 400 when quiz date is in the past")
    void createQuiz_pastDate_returns400() throws Exception {
        when(quizService.add(any(QuizRequestDTO.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.fail("Quiz date and time must be in the future")));

        QuizRequestDTO dto = new QuizRequestDTO();
        dto.setJoinCode("PAST01");

        mockMvc.perform(post("/quiz/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /quiz/all
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /quiz/all: returns 200 with list of all quizzes")
    void getAllQuizzes_returns200WithList() throws Exception {
        Quiz q = buildQuiz("ALL01");
        when(quizService.getAllQuizzes())
                .thenReturn(ResponseEntity.ok(ApiResponse.ok("Quizzes retrieved successfully", List.of(q))));

        mockMvc.perform(get("/quiz/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Quizzes retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("GET /quiz/all: returns 200 with empty array when no quizzes exist")
    void getAllQuizzes_noQuizzes_returnsEmptyArray() throws Exception {
        when(quizService.getAllQuizzes())
                .thenReturn(ResponseEntity.ok(ApiResponse.ok("Quizzes retrieved successfully", List.of())));

        mockMvc.perform(get("/quiz/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /quiz/upcoming
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /quiz/upcoming: returns 200 with upcoming quizzes")
    void getUpcomingQuizzes_returns200() throws Exception {
        Quiz q = buildQuiz("UP01");
        when(quizService.getUpcomingQuizzes())
                .thenReturn(ResponseEntity.ok(ApiResponse.ok("Upcoming quizzes retrieved successfully", List.of(q))));

        mockMvc.perform(get("/quiz/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POST /quiz/join/{code}
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /quiz/join/{code}: returns 202 when quiz is active")
    void joinQuiz_activeQuiz_returns202() throws Exception {
        Quiz q = buildQuiz("LIVE01");
        when(quizService.handleQuizJoin("LIVE01"))
                .thenReturn(ResponseEntity.status(HttpStatus.ACCEPTED)
                        .body(ApiResponse.ok("Quiz is active, you can join", q)));

        mockMvc.perform(post("/quiz/join/LIVE01"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Quiz is active, you can join"));
    }

    @Test
    @DisplayName("POST /quiz/join/{code}: returns 404 when join code is invalid")
    void joinQuiz_invalidCode_returns404() throws Exception {
        when(quizService.handleQuizJoin("NONE"))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail("Invalid code, please check again")));

        mockMvc.perform(post("/quiz/join/NONE"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid code, please check again"));
    }

    @Test
    @DisplayName("POST /quiz/join/{code}: returns 403 when quiz is not active")
    void joinQuiz_notActive_returns403() throws Exception {
        when(quizService.handleQuizJoin("OVER01"))
                .thenReturn(ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.fail("Quiz has not started yet or is already over")));

        mockMvc.perform(post("/quiz/join/OVER01"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POST /quiz/isCorrect
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /quiz/isCorrect: returns true when answer is correct")
    void isCorrect_correctAnswer_returnsTrue() throws Exception {
        IsCorrectRequest request = new IsCorrectRequest();
        request.setQuestionId(1);
        request.setQuizId(10);
        request.setSelectedOption("B");

        when(quizService.isCorrect(any(IsCorrectRequest.class))).thenReturn(true);

        mockMvc.perform(post("/quiz/isCorrect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("POST /quiz/isCorrect: returns false when answer is wrong")
    void isCorrect_wrongAnswer_returnsFalse() throws Exception {
        IsCorrectRequest request = new IsCorrectRequest();
        request.setQuestionId(1);
        request.setQuizId(10);
        request.setSelectedOption("A");

        when(quizService.isCorrect(any(IsCorrectRequest.class))).thenReturn(false);

        mockMvc.perform(post("/quiz/isCorrect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POST /quiz/isValid/{id}
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /quiz/isValid/{id}: returns true when quiz id is valid")
    void isValidQuiz_validId_returnsTrue() throws Exception {
        when(leaderBoards.isValidQuizId("42")).thenReturn(true);

        mockMvc.perform(post("/quiz/isValid/42"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("POST /quiz/isValid/{id}: returns false when quiz id is invalid")
    void isValidQuiz_invalidId_returnsFalse() throws Exception {
        when(leaderBoards.isValidQuizId("999")).thenReturn(false);

        mockMvc.perform(post("/quiz/isValid/999"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}
