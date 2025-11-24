package com.haisy.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.haisy.app.Model.Quiz;

@Repository
public interface QuizRepo extends JpaRepository<Quiz,Integer>{
    
}
