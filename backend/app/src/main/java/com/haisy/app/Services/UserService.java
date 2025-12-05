package com.haisy.app.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haisy.app.Model.User;
import com.haisy.app.Repository.UserRepo;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public boolean isUserExist(String userName){
        return userRepo.existsByUserName(userName);
    }
    public boolean setuserName(String oldName, String newName){
        if(isUserExist(newName)){
            return false;
        }
        Optional<User> record = userRepo.findByUserName(oldName);
        User user = record.get();
        user.setUserName(newName);
        userRepo.save(user);
        return true;
    }
    public boolean addNewUser(String userName){
        User user = new User();
        user.setUserName(userName);
        if(isUserExist(userName)){
            return false;
        }else{
            userRepo.save(user);
            if(isUserExist(userName)){
                return true;
            }
        }

        return false;
    }
}
