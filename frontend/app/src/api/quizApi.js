import axiosClient from "./axiosClient";

export const quizApi = {
    joinQuiz(code){
        return axiosClient.post("/joinQuiz/"+code)
    },
}