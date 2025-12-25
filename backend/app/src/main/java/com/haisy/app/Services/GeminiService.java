package com.haisy.app.Services;

import org.springframework.stereotype.Service;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.haisy.app.Logs.FileLogger;

@Service
public class GeminiService {

    public String generateQuestions(String topic, int n, int difficulty) {

        FileLogger.info("Generating quiz questions | topic=" + topic +
                ", count=" + n + ", difficulty=" + difficulty);

        Client client = new Client();

        String input = makePrompt(topic, n, difficulty);

        FileLogger.debug("Prompt generated for Gemini API");

        GenerateContentResponse response = client.models.generateContent(
                "gemini-2.5-flash",
                input,
                null
        );

        client.close();

        FileLogger.info("Received response from Gemini API");

        return response.text();
    }

    public String makePrompt(String t, int n, int d) {

        FileLogger.debug("Preparing prompt for topic: " + t);

        String prompt = String.format("""
                You are an API that generates quiz questions in a strict JSON format.
                Never use markdown, bold text, emojis, or headings.

                INPUT:
                topic: %s
                numberOfQuestions: %d
                difficulty: %d

                RULES:
                - Output ONLY valid JSON
                - No extra text
                - No explanations
                - Exactly %d questions
                - Each question must contain:
                    - question
                    - optionA
                    - optionB
                    - optionC
                    - optionD
                    - correctOption (A/B/C/D)

                Return only the JSON array.
                """, t, n, d, n);

        FileLogger.debug("Prompt successfully created");

        return prompt;
    }
}
