package com.haisy.app.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Participant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int participantId;
    String userName;
    String email;
    int score;
    
    @ManyToOne
    @JsonBackReference
    QuizResultSet quizResultSet;

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public QuizResultSet getQuizResultSet() {
        return quizResultSet;
    }

    public void setQuizResultSet(QuizResultSet quizResultSet) {
        this.quizResultSet = quizResultSet;
    }
}
