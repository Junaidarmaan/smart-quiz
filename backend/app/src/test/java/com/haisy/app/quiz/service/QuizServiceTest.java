package com.haisy.app.quiz.service;

import com.haisy.app.common.dto.ApiResponse;
import com.haisy.app.quiz.dto.IsCorrectRequest;
import com.haisy.app.quiz.dto.QuizRequestDTO;
import com.haisy.app.quiz.mapper.QuizDtoMapper;
import com.haisy.app.quiz.model.Question;
import com.haisy.app.quiz.model.Quiz;
import com.haisy.app.quiz.model.Schedule;
import com.haisy.app.quiz.repository.QuestionsRepository;
import com.haisy.app.quiz.repository.QuizRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuizService Tests")
class QuizServiceTest {

    @Mock private QuizRepo quizRepo;
    @Mock private QuizDtoMapper mapper;
    @Mock private QuestionsRepository questionRepo;

    @InjectMocks
    private QuizService quizService;

    // ── helpers ──────────────────────────────────────────────────────────────

    private Quiz buildQuiz(String code, LocalDateTime dateTime, int durationMinutes) {
        Schedule schedule = new Schedule();
        schedule.setDateTime(dateTime);
        schedule.setDuration(durationMinutes);

        Question q = new Question();
        q.setQuestion("What is 2+2?");
        q.setOptionA("3"); q.setOptionB("4");
        q.setOptionC("5"); q.setOptionD("6");
        q.setCorrectOption("B");

        Quiz quiz = new Quiz();
        quiz.setJoinCode(code);
        quiz.setSchedule(schedule);
        quiz.setQuestions(List.of(q));
        return quiz;
    }

    private QuizRequestDTO dummyDto() {
        QuizRequestDTO dto = new QuizRequestDTO();
        dto.setJoinCode("ABC123");
        return dto;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // add()
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("add: returns 201 CREATED when quiz is valid and code is unique")
    void add_validQuiz_returns201() {
        Quiz quiz = buildQuiz("ABC123", LocalDateTime.now().plusDays(1), 30);
        when(mapper.toQuizEntity(any())).thenReturn(quiz);
        when(quizRepo.existsByJoinCode("ABC123")).thenReturn(false);

        ResponseEntity<ApiResponse<Void>> response = quizService.add(dummyDto());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getMessage()).isEqualTo("Quiz created successfully");
        verify(quizRepo).save(quiz);
    }

    @Test
    @DisplayName("add: returns 409 CONFLICT when join code is already in use")
    void add_duplicateJoinCode_returns409() {
        Quiz quiz = buildQuiz("DUPE01", LocalDateTime.now().plusDays(1), 30);
        when(mapper.toQuizEntity(any())).thenReturn(quiz);
        when(quizRepo.existsByJoinCode("DUPE01")).thenReturn(true);

        ResponseEntity<ApiResponse<Void>> response = quizService.add(dummyDto());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Join code already in use, please choose another");
        verify(quizRepo, never()).save(any());
    }

    @Test
    @DisplayName("add: returns 400 BAD_REQUEST when quiz date is in the past")
    void add_pastDate_returns400() {
        Quiz quiz = buildQuiz("PAST01", LocalDateTime.now().minusDays(1), 30);
        when(mapper.toQuizEntity(any())).thenReturn(quiz);
        when(quizRepo.existsByJoinCode("PAST01")).thenReturn(false);

        ResponseEntity<ApiResponse<Void>> response = quizService.add(dummyDto());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Quiz date and time must be in the future");
        verify(quizRepo, never()).save(any());
    }

    @Test
    @DisplayName("add: sets back-reference on every question before saving")
    void add_validQuiz_setsQuizReferenceOnAllQuestions() {
        Quiz quiz = buildQuiz("REF123", LocalDateTime.now().plusDays(1), 30);
        when(mapper.toQuizEntity(any())).thenReturn(quiz);
        when(quizRepo.existsByJoinCode("REF123")).thenReturn(false);

        quizService.add(dummyDto());

        quiz.getQuestions().forEach(q ->
                assertThat(q.getQuiz()).isEqualTo(quiz)
        );
    }

    @Test
    @DisplayName("add: returns 400 when quiz is scheduled for exact current moment (past boundary)")
    void add_quizScheduledNow_returns400() {
        // Slightly in the past to guarantee isBefore check triggers
        Quiz quiz = buildQuiz("NOW001", LocalDateTime.now().minusSeconds(2), 30);
        when(mapper.toQuizEntity(any())).thenReturn(quiz);
        when(quizRepo.existsByJoinCode("NOW001")).thenReturn(false);

        ResponseEntity<ApiResponse<Void>> response = quizService.add(dummyDto());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // getAllQuizzes()
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getAllQuizzes: returns 200 with all quizzes from repository")
    void getAllQuizzes_returnsAllQuizzes() {
        Quiz q1 = buildQuiz("A1", LocalDateTime.now().plusDays(1), 30);
        Quiz q2 = buildQuiz("B2", LocalDateTime.now().plusDays(2), 45);
        when(quizRepo.findAll()).thenReturn(List.of(q1, q2));

        ResponseEntity<ApiResponse<List<Quiz>>> response = quizService.getAllQuizzes();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getData()).hasSize(2);
        assertThat(response.getBody().getMessage()).isEqualTo("Quizzes retrieved successfully");
    }

    @Test
    @DisplayName("getAllQuizzes: returns 200 with empty list when no quizzes exist")
    void getAllQuizzes_noQuizzes_returnsEmptyList() {
        when(quizRepo.findAll()).thenReturn(List.of());

        ResponseEntity<ApiResponse<List<Quiz>>> response = quizService.getAllQuizzes();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getData()).isEmpty();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // getUpcomingQuizzes()
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("getUpcomingQuizzes: returns 200 with only future quizzes")
    void getUpcomingQuizzes_returnsFutureQuizzesOnly() {
        Quiz upcoming = buildQuiz("UP01", LocalDateTime.now().plusDays(1), 30);
        when(quizRepo.findByScheduleDateTimeAfter(any(LocalDateTime.class))).thenReturn(List.of(upcoming));

        ResponseEntity<ApiResponse<List<Quiz>>> response = quizService.getUpcomingQuizzes();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getData()).hasSize(1);
        assertThat(response.getBody().getMessage()).isEqualTo("Upcoming quizzes retrieved successfully");
    }

    @Test
    @DisplayName("getUpcomingQuizzes: returns 200 with empty list when no upcoming quizzes")
    void getUpcomingQuizzes_noUpcoming_returnsEmptyList() {
        when(quizRepo.findByScheduleDateTimeAfter(any(LocalDateTime.class))).thenReturn(List.of());

        ResponseEntity<ApiResponse<List<Quiz>>> response = quizService.getUpcomingQuizzes();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getData()).isEmpty();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // isCodeExist()
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("isCodeExist: returns true when join code exists")
    void isCodeExist_existingCode_returnsTrue() {
        when(quizRepo.existsByJoinCode("EXISTS")).thenReturn(true);
        assertThat(quizService.isCodeExist("EXISTS")).isTrue();
    }

    @Test
    @DisplayName("isCodeExist: returns false when join code does not exist")
    void isCodeExist_nonExistentCode_returnsFalse() {
        when(quizRepo.existsByJoinCode("GHOST")).thenReturn(false);
        assertThat(quizService.isCodeExist("GHOST")).isFalse();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // handleQuizJoin()
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("handleQuizJoin: returns 404 when join code does not exist")
    void handleQuizJoin_invalidCode_returns404() {
        when(quizRepo.existsByJoinCode("INVALID")).thenReturn(false);

        ResponseEntity<ApiResponse<Quiz>> response = quizService.handleQuizJoin("INVALID");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid code, please check again");
    }

    @Test
    @DisplayName("handleQuizJoin: returns 202 ACCEPTED when quiz is currently active")
    void handleQuizJoin_activeQuiz_returns202() {
        // Quiz started 5 minutes ago, lasts 30 minutes → active now
        Quiz quiz = buildQuiz("LIVE01", LocalDateTime.now().minusMinutes(5), 30);
        when(quizRepo.existsByJoinCode("LIVE01")).thenReturn(true);
        when(quizRepo.findByJoinCode("LIVE01")).thenReturn(quiz);

        ResponseEntity<ApiResponse<Quiz>> response = quizService.handleQuizJoin("LIVE01");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getMessage()).isEqualTo("Quiz is active, you can join");
        assertThat(response.getBody().getData()).isNotNull();
    }

    @Test
    @DisplayName("handleQuizJoin: returns 403 FORBIDDEN when quiz has not started yet")
    void handleQuizJoin_notStartedYet_returns403() {
        // Quiz starts tomorrow
        Quiz quiz = buildQuiz("SOON01", LocalDateTime.now().plusHours(2), 30);
        when(quizRepo.existsByJoinCode("SOON01")).thenReturn(true);
        when(quizRepo.findByJoinCode("SOON01")).thenReturn(quiz);

        ResponseEntity<ApiResponse<Quiz>> response = quizService.handleQuizJoin("SOON01");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Quiz has not started yet or is already over");
    }

    @Test
    @DisplayName("handleQuizJoin: returns 403 FORBIDDEN when quiz has already ended")
    void handleQuizJoin_quizAlreadyOver_returns403() {
        // Quiz started 2 hours ago, lasted 30 minutes → already over
        Quiz quiz = buildQuiz("OVER01", LocalDateTime.now().minusHours(2), 30);
        when(quizRepo.existsByJoinCode("OVER01")).thenReturn(true);
        when(quizRepo.findByJoinCode("OVER01")).thenReturn(quiz);

        ResponseEntity<ApiResponse<Quiz>> response = quizService.handleQuizJoin("OVER01");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Quiz has not started yet or is already over");
    }

    @Test
    @DisplayName("handleQuizJoin: returns 202 when quiz starts exactly now (boundary)")
    void handleQuizJoin_startsExactlyNow_returns202() {
        // starts 1 second ago: isAfter(start) || isEqual(start) → true; isBefore(end) → true
        Quiz quiz = buildQuiz("EXACT1", LocalDateTime.now().minusSeconds(1), 60);
        when(quizRepo.existsByJoinCode("EXACT1")).thenReturn(true);
        when(quizRepo.findByJoinCode("EXACT1")).thenReturn(quiz);

        ResponseEntity<ApiResponse<Quiz>> response = quizService.handleQuizJoin("EXACT1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // isCorrect()
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("isCorrect: returns true when selected option matches the correct answer")
    void isCorrect_correctAnswer_returnsTrue() {
        Question q = new Question();
        q.setCorrectOption("B");

        IsCorrectRequest request = new IsCorrectRequest();
        request.setQuestionId(1);
        request.setQuizId(10);
        request.setSelectedOption("B");

        when(questionRepo.findByIdAndQuizQuizId(1, 10)).thenReturn(q);

        assertThat(quizService.isCorrect(request)).isTrue();
    }

    @Test
    @DisplayName("isCorrect: returns false when selected option does not match")
    void isCorrect_wrongAnswer_returnsFalse() {
        Question q = new Question();
        q.setCorrectOption("A");

        IsCorrectRequest request = new IsCorrectRequest();
        request.setQuestionId(2);
        request.setQuizId(10);
        request.setSelectedOption("C");

        when(questionRepo.findByIdAndQuizQuizId(2, 10)).thenReturn(q);

        assertThat(quizService.isCorrect(request)).isFalse();
    }

    @Test
    @DisplayName("isCorrect: returns true for option D when correct answer is D")
    void isCorrect_optionD_correctAnswer_returnsTrue() {
        Question q = new Question();
        q.setCorrectOption("D");

        IsCorrectRequest request = new IsCorrectRequest();
        request.setQuestionId(3);
        request.setQuizId(10);
        request.setSelectedOption("D");

        when(questionRepo.findByIdAndQuizQuizId(3, 10)).thenReturn(q);

        assertThat(quizService.isCorrect(request)).isTrue();
    }
}
