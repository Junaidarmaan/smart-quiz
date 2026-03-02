import axiosClient from "./axiosClient";

export const quizApi = {
    joinQuiz(code) {
        return axiosClient.post(`/quiz/join/${code}`);
    },

    createQuiz(data) {
        return axiosClient.post("/quiz/create", data);
    },

    getAllQuizzes() {
        return axiosClient.get("/quiz/all");
    },

    getUpcomingQuizzes() {
        return axiosClient.get("/quiz/upcoming");
    },

    isCorrect(data) {
        return axiosClient.post("/quiz/isCorrect", data);
    },

    getCurrentQuestion(data) {
        return axiosClient.post("/quiz/currentQuestion", data);
    },

    generateQuestions(data) {
        return axiosClient.post("/gemini/generate", data);
    }
};