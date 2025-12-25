package com.haisy.app.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haisy.app.Model.QuizResultSet;
import com.haisy.app.Repository.QuizResultSetRepository;

@Service
public class QuizResultService {
    @Autowired
    QuizResultSetRepository repo;

    public void saveResultSet(QuizResultSet quizResultSet) {
        repo.save(quizResultSet);
    }


}
