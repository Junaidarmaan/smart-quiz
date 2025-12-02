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
        String receivedCode = quiz.getJoinCode();
        if(quizRepo.existsByJoinCode(receivedCode)){
            response.put("message", "entered joinCode already in use please choose another");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        response.put("data", "Quiz created successfully check upcoming quizzes to verify");
        //for foreign key if you remove it the schdule ie date time beomes null and cant map to the questions table
        quiz.getQuestions().forEach((q)->{
            q.setQuiz(quiz);
        });

        LocalDateTime today = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        //checking if the date and time is valid IE it must be in the ner future never be the past
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

    public ResponseEntity<Map<String,Object>> getUpcomingQuizzes(){
        LocalDateTime today = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        List<Quiz> upcomingQuizzes = quizRepo.findByScheduleDateTimeAfter(today);
        Map<String,Object> response = new HashMap<>();
        response.put("data", upcomingQuizzes);
        response.put("message", "Upcoming quizzes retrieved successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public boolean isCodeExist(String code){
        return quizRepo.existsByJoinCode(code);
    }

    public ResponseEntity<Map<String,Object>> handleQuizJoin(String code){
        boolean isValidCode = isCodeExist(code);
        Map<String,Object> map = new HashMap<>();

        if(!isValidCode){
            map.put("action",false);
            map.put("message", "the provided code is invalid please check again ");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }
        Quiz quiz = quizRepo.findByJoinCode(code);
        

        int duration = quiz.getSchedule().getDuration();
        LocalDateTime start = quiz.getSchedule().getDateTime();
        LocalDateTime end = start.plusMinutes(duration);
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        boolean flag = (now.isAfter(start) || now.isEqual(start)) && (now.isBefore(end) || (now.isEqual(end)));
        if(flag){
            map.put("action",true);
            map.put("message","you can join the quiz you arrived at valid Schedule");
        }else{
            map.put("action",false);
            map.put("start",start);
            map.put("end",end);
            if(now.isBefore(start)){
                map.put("message","isEarly");
                return ResponseEntity.status(HttpStatus.TOO_EARLY).body(map);
            }else{
                map.put("message","isLate");
                return ResponseEntity.status(HttpStatus.TOO_EARLY).body(map);

            }

        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(map);
        
    }

    public boolean isCorrect(Map<String,Object> map){
        String question = map.get("question").toString();
        int id = Integer.parseInt(map.get("id").toString());
        String quizId = map.get("joinCode").toString();
        String correctoption = map.get("correctOption").toString();
        Quiz q = quizRepo.findByJoinCode(quizId);
        // List<quizQuestions> qs = q.getQuestions();
        boolean result = false;
        return result;
    }
}
