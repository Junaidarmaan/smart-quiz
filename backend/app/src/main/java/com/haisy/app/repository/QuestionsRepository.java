package com.haisy.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.haisy.app.model.Question;

@Repository
public interface QuestionsRepository extends JpaRepository<Question,Integer> {
    Question findByIdAndQuizQuizId(int id, int quizId);
}
