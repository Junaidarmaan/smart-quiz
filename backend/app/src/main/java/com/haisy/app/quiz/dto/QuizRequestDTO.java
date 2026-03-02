package com.haisy.app.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class QuizRequestDTO {

    @NotEmpty(message = "Questions list cannot be empty")
    private List<QuizQuestions> questions;

    @NotNull(message = "Schedule is required")
    private QuizSchedule schedule;

    @NotBlank(message = "Join code is required")
    private String joinCode;

    public List<QuizQuestions> getQuestions() { return questions; }
    public void setQuestions(List<QuizQuestions> questions) { this.questions = questions; }

    public QuizSchedule getSchedule() { return schedule; }
    public void setSchedule(QuizSchedule schedule) { this.schedule = schedule; }

    public String getJoinCode() { return joinCode; }
    public void setJoinCode(String joinCode) { this.joinCode = joinCode; }
}