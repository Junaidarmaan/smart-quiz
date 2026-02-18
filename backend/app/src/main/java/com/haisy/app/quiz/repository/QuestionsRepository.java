package com.haisy.app.quiz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.haisy.app.quiz.model.Question;

@Repository
public interface QuestionsRepository extends JpaRepository<Question, Integer> {
    Optional<Question> findByIdAndQuizQuizId(int id, int quizId);
}
