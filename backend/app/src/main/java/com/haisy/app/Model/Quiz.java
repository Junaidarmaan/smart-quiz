package com.haisy.app.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String topic;
    boolean isManual = false;
    String questionAndAnswers;
    
    
    @Override
    public String toString() {
        return "Quiz [id=" + id + ", isManual=" + isManual + ", questionAndAnswers=" + questionAndAnswers + "]";
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getTopic() {
        return topic;
    }


    public void setTopic(String topic) {
        this.topic = topic;
    }


    public boolean isManual() {
        return isManual;
    }


    public void setManual(boolean isManual) {
        this.isManual = isManual;
    }


    public String getQuestionAndAnswers() {
        return questionAndAnswers;
    }


    public void setQuestionAndAnswers(String questionAndAnswers) {
        this.questionAndAnswers = questionAndAnswers;
    }
}
