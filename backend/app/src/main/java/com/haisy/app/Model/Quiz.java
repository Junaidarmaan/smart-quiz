package com.haisy.app.Model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer quizId;
    
    String joinCode;
    @OneToMany(mappedBy = "quiz" ,cascade = CascadeType.ALL)
    @JsonManagedReference
    List<Question> questions;

    @Embedded
    Schedule schedule;

    @OneToOne(mappedBy = "quiz", cascade = CascadeType.ALL)
    @JsonManagedReference
    QuizResultSet quizResults;

    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }

    public QuizResultSet getQuizResults() {
        return quizResults;
    }

    public void setQuizResults(QuizResultSet quizResults) {
        this.quizResults = quizResults;
    }

   
}
