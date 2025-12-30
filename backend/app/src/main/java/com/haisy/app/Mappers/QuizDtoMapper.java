package com.haisy.app.Mappers;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.haisy.app.DTO.QuizJoinResponseDto;
import com.haisy.app.DTO.QuizQuestions;
import com.haisy.app.DTO.QuizRequestDTO;
import com.haisy.app.DTO.QuizSchedule;
import com.haisy.app.Model.Question;
import com.haisy.app.Model.Quiz;
import com.haisy.app.Model.Schedule;


@Mapper(componentModel = "spring") // this line becuase to tell spring create its bean at start in simple to create intsace in IOC
public interface QuizDtoMapper {
    @Mapping(target="quizId", ignore=true)
    Quiz toQuizEntity(QuizRequestDTO dto);
    @Mapping(target="quiz", ignore=true)
    @Mapping(target="id", ignore=true)

    Question toQuestionEntity(QuizQuestions question);

    @Mapping(target="dateTime"
        ,expression = "java(combine((dto)))"
    )
    Schedule toScheduleEntity(QuizSchedule dto);

    default LocalDateTime combine(QuizSchedule schedule){
        return LocalDateTime.of(schedule.getDate(), schedule.getTime());
    }
    @Mapping(source = "questions", target = "data")
    QuizJoinResponseDto toQuizJoinResponseDto(Quiz q);
    QuizQuestions toQuizQuestions(Question q);
}
