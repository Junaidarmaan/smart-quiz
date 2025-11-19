package com.haisy.app.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;  

import com.haisy.app.Model.Quiz;
import com.haisy.app.Repository.QuizRepo;
    
@Service
public class QuizService {            
    @Autowired
    private QuizRepo quizRepo;

    public String add(Quiz quiz){
        return "Successfully added the " + quizRepo.save(quiz).toString();
    } 
}
