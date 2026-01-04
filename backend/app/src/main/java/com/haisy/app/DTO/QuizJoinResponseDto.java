package com.haisy.app.DTO;

import java.util.List;


public class QuizJoinResponseDto {
    String message;
    boolean action;
    List<QuizQuestions> data;
    Integer quizId;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public boolean isAction() {
        return action;
    }
    public void setAction(boolean action) {
        this.action = action;
    }
    public List<QuizQuestions> getData() {
        return data;
    }
    public void setData(List<QuizQuestions> data) {
        this.data = data;
    }
    public Integer getQuizId() {
        return quizId;
    }
    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }
    
    
}
