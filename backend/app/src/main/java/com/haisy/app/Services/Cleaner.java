package com.haisy.app.Services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
    public void cleanDb(){
        List<Quiz>  quizes = quizService.getAllQuizzes();
        System.out.println("entered cleaner");
        for(Quiz quiz : quizes){
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
            LocalDateTime endTime = quiz.getSchedule().getDateTime().plusMinutes(quiz.getSchedule().getDuration());
            QuizResultSet resultSet = new QuizResultSet();
            List<Participant> participants = new ArrayList<>();
            if(now.isAfter(endTime)){
                boolean isvalid = leaderBoards.isValidQuizId(quiz.getJoinCode());
                if(isvalid){
                    List<UserProfile> results = leaderBoards.getRankings(quiz.getJoinCode());
                    System.out.println(quiz.getJoinCode() + " found in the map hence creating result set and  deleting from quiz table");
                    for(UserProfile u : results){
                        Participant p = new Participant();
                        p.setUserName(u.getUserName());
                        p.setScore(u.getScore());
                        participants.add(p);
                    }
                    resultSet.setParticipants(participants);
                    quizResultService.saveResultSet(resultSet);
                    quizService.removeQuiz(quiz.getJoinCode());
                    leaderBoards.removeQuiz(quiz.getJoinCode());
                    
                }else{
                    System.out.println(quiz.getJoinCode() + " not found in the map hence deleting from quiz table");
                    quizService.removeQuiz(quiz.getJoinCode());
                }
            }
        }
        
    }
}
