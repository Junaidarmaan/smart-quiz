package com.haisy.app.quiz.controller;


import com.haisy.app.quiz.dto.GeminiRequest;
import com.haisy.app.quiz.service.GeminiService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gemini")
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/generate")
    public String generateQuestions(@RequestBody GeminiRequest request) {
        return geminiService.generateQuestions(
            request.getTopic(),
            request.getQuantity(),
            request.getDifficulty()
        );
    }
}