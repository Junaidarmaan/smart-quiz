package com.haisy.app.Services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.haisy.app.Model.Quiz;
import com.haisy.app.Services.WebSocket.LeaderBoards;
import com.haisy.app.Services.WebSocket.UserProfile;

@Service
public class Cleaner {
    @Autowired
    QuizService quizService;

    @Autowired
    LeaderBoards leaderBoards;

    @Scheduled(fixedRate = 60000)
    public void cleanDb(){
        List<Quiz>  quizes = quizService.getAllQuizzes();
        System.out.println("entered cleaner");
        for(Quiz quiz: quizes){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime quizEndTime = quiz.getSchedule().getDateTime().plusMinutes(quiz.getSchedule().getDuration());
            String joinCode = quiz.getJoinCode();
            if(now.isAfter(quizEndTime)){
                // add results to db
                List<UserProfile> quizResults = leaderBoards.getRankings(joinCode);
                quizService.removeQuiz(joinCode);
                System.out.println("the finl results are "+ quizResults);
                leaderBoards.removeQuiz(joinCode);
            }
        }
    }
}
