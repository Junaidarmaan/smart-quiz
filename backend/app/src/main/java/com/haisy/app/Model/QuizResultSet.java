package com.haisy.app.Model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class QuizResultSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int quizResultSetId;

    String quizJoinCode;

    @OneToOne
    @JsonBackReference
    Quiz quiz;

    @OneToMany(mappedBy = "quizResultSet", cascade = CascadeType.ALL)
    @JsonManagedReference
    List<Participant> participants;

    public int getQuizResultSetId() {
        return quizResultSetId;
    }

    public void setQuizResultSetId(int quizResultSetId) {
        this.quizResultSetId = quizResultSetId;
    }

    public String getQuizJoinCode() {
        return quizJoinCode;
    }

    public void setQuizJoinCode(String quizJoinCode) {
        this.quizJoinCode = quizJoinCode;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

}
