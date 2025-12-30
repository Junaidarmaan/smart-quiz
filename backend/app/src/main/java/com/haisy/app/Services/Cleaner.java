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

    // @Scheduled(fixedRate = 60000)
    public void cleanDb(){
               
    }
}
