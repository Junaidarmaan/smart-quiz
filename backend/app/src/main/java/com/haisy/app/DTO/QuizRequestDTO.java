package com.haisy.app.DTO;

import java.util.List;

public class QuizRequestDTO {
    List<QuizQuestions> questions;
    QuizSchedule schedule;
    public List<QuizQuestions> getQuestions() {
        return questions;
    }
    public void setQuestions(List<QuizQuestions> questions) {
        this.questions = questions;
    }
    public QuizSchedule getSchedule() {
        return schedule;
    }
    public void setSchedule(QuizSchedule schedule) {
        this.schedule = schedule;
    }
    @Override
    public String toString() {
        return "QuizRequestDTO [questions=" + questions + ", schedule=" + schedule + "]";
    }
}
