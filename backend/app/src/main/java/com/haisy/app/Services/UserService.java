package com.haisy.app.Services;

import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.haisy.app.Model.User;
import com.haisy.app.Repository.UserRepo;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public ResponseEntity<Map<String,Object>> signUp(User user) {
        Map<String,Object> Response = new HashMap<>();

        if (!isUserExists(user)) {
            String role = user.getRole();
            if(role.equals("student") || role.equals("teacher")){
                addUser(user);
                Response.put("message","Signup was successfull");
                Response.put("status",true);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(Response);
            }else{
                Response.put("message","Invalid role , it must be either student or teacher");
                Response.put("status",false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response);
            }
        }
        Response.put("message","The user with provided email alredy exits you better login");
        Response.put("status",false);
        return ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES).body(Response);
    }

    public ResponseEntity<String> longIn(User user) {
        Optional<User> x = userRepo.findByEmail(user.getEmail());
        if (x.isPresent()) {
            if (user.getUserPassword().equals(x.get().getUserPassword())) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("logged In");
            } else {
                return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).body("wrong password");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
    }

    public String addUser(User user) {
        return "Successfully added the user : " + userRepo.save(user).toString();
    }

    public boolean isUserExists(User user) {
        Optional<User> x = userRepo.findByEmail(user.getEmail());
        if (x.isEmpty()) {
            return false;
        }
        return true;
    }
}
