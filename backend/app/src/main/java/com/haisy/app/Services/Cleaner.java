package com.haisy.app.Services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.haisy.app.Logs.FileLogger;
import com.haisy.app.Model.Participant;
import com.haisy.app.Model.Quiz;
import com.haisy.app.Model.QuizResultSet;
import com.haisy.app.Services.WebSocket.LeaderBoards;
import com.haisy.app.Services.WebSocket.UserProfile;


@Service
public class Cleaner {
    @Autowired
    QuizService quizService;

    @Autowired
    QuizResultService quizResultService;
    @Autowired
    LeaderBoards leaderBoards; 

    @Scheduled(fixedRate = 60000)
    public void storeResult(){
        System.out.println("cleaner started.....");
        List<Quiz> quizes = quizService.getAllQuizzes();
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        for(Quiz q : quizes){
            LocalDateTime quizDateTime = q.getSchedule().getDateTime().plusMinutes(q.getSchedule().getDuration());
            if(now.isAfter(quizDateTime)){
                FileLogger.info("The quiz wiht quiz joinCode" + q.getJoinCode() + " has expired");
            }else{
                FileLogger.info("The quiz wiht quiz joinCode" + q.getJoinCode() + " is active");

            }
        }
    }
}
