package com.haisy.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.haisy.app.Model.Question;

@Repository
public interface QuestionsRepository extends JpaRepository<Question,Integer> {
    Question findByIdAndQuizId(int id, int quizId);
}
