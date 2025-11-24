package com.haisy.app.Services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.haisy.app.DTO.QuizRequestDTO;
import com.haisy.app.Mappers.QuizDtoMapper;
import com.haisy.app.Model.Quiz;
import com.haisy.app.Repository.QuizRepo;
    
@Service
public class QuizService {            
    @Autowired
    private QuizRepo quizRepo;
    @Autowired
    private QuizDtoMapper mapper;

    public ResponseEntity<Map<String,String>> add(QuizRequestDTO dto){
        Quiz quiz = mapper.toQuizEntity(dto);
        Map<String,String> response = new HashMap<>();
        response.put("data", "Quiz created successfully check upcoming quizzes to verify");
        //for foreign key if you remove it the schdule ie date time beomes null and cant map to the questions table
        quiz.getQuestions().forEach((q)->{
            q.setQuiz(quiz);
        });

        //checking if the date and time is valid IE it must be in the ner future never be the past
        LocalDateTime today = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        if(quiz.getSchedule().getDateTime().isBefore(today)){
                response.put("data", "Quiz date and time must be in the future");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        quizRepo.save(quiz);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } 

    public List<Quiz> getAllQuizzes(){
        return quizRepo.findAll();
    }

    // public ResponseEntity<List<Quiz>> getUpcomingQuizzes(){
    //     LocalDate today = LocalDate.now(ZoneId.of("asia/kolkata"));
    //     LocalTime now = LocalTime.now(ZoneId.of("asia/kolkata"));
    //     // List<Quiz> quizzes = quizRepo.findAllUpcomingQuizzes(today, now);
    //     return ResponseEntity.status(HttpStatus.OK).body(quizzes);
    // }
}
