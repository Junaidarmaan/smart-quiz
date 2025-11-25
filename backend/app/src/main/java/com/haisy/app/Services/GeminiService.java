package com.haisy.app.Services;

import org.springframework.stereotype.Service;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

@Service
public class GeminiService {

    public String generateQuestions(String topic, int n, int difficulty) {
        Client client = new Client();
        String input = makePrompt(topic, n, difficulty);
        GenerateContentResponse response = client.models.generateContent(
                "gemini-2.5-flash",
                input,
                null);
        client.close();

        return response.text();
    }

    public String makePrompt(String t, int n, int d) { // t->topic, n -> number of questions , d -> difficulty level
        String prompt = String.format("""
                You are an API that generates quiz questions in a strict JSON format.
                Never use markdown, bold text, emojis, headings, slashes or any styling.
                your entir output must be plain json with out any styling addons like double quotes slashes"
                

                INPUT YOU WILL RECEIVE:
                1. topic: %s
                2. numberOfQuestions: %d
                3. difficulty: %d (1 to 10, where 10 is extremely difficult)

                YOUR TASK:
                Generate exactly %d questions about the %s.
                The complexity of the questions should match the given difficulty level.
                Difficulty 1 is very simple, difficulty 10 is highly challenging and advanced.

                RESPONSE FORMAT:
                Return ONLY a JSON array (no text outside the array).
                Each element MUST match the following Java DTO structure exactly:

                {
                  "question": "string",
                  "optionA": "string",
                  "optionB": "string",
                  "optionC": "string",
                  "optionD": "string",
                  "correctOption": "A or B or C or D"
                }

                STRICT RULES:
                - No Markdown
                - No Bold texts
                - No Headings
                - No extra characters like **,__,##, /, >, < ,\\, etc unless it itself is part of response
                - no natural language or explaination before or after json
                - Each question must have exactly 4 options.
                - correctOption must be only: "A", "B", "C", or "D".
                - Do not add explanations.
                - Do not wrap the JSON in code blocks.
                - The array length must match numberOfQuestions exactly.
                - Ensure all questions strictly stay within the requested topic.

                OUTPUT:
                Only the JSON array of QuizQuestions.

                """,t,n,d,n,t);

                return prompt;
    }
}
