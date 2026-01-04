package com.haisy.app.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haisy.app.Model.Results;
import com.haisy.app.Repository.ResultRepository;
import com.haisy.app.Services.Excel.ExcelService;
import com.haisy.app.Services.WebSocket.UserProfile;

@Service
public class ResultService {
    @Autowired
    ResultRepository resultRepository;

    @Autowired
    ExcelService xl;
    public boolean addResult(List<UserProfile> obj,String quizId){
        byte[] arr = xl.generateLeaderboardExcel(quizId, obj);
        Results result= new Results();
        result.setJoinCode(quizId);
        result.setResultXL(arr);
        Results flag = resultRepository.save(result);
        if(flag == null){
            return false;
        }
        return true;
    }

    public Optional<Results> getResultXL(String joinCode){
        Optional<Results> status = resultRepository.findByJoinCode(joinCode);
        return status;
    }
}
