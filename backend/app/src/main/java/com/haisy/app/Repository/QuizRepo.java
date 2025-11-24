package com.haisy.app.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.haisy.app.Model.Quiz;

@Repository
public interface QuizRepo extends JpaRepository<Quiz,Integer>{

    // this is for getting upcoming quizzes it says like in the quiz there is schedule object in that object there dateTime field
    List<Quiz> findByScheduleDateTimeAfter(LocalDateTime dateTime);
}
