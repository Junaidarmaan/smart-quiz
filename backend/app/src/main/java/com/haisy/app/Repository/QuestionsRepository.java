package com.haisy.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haisy.app.Model.Question;

public interface QuestionsRepository extends JpaRepository<Question,Integer> {
    Question findByIdAndQuizId(int id, int quizId);
}
