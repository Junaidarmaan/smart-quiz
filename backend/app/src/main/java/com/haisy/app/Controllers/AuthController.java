package com.haisy.app.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haisy.app.Services.UserService;

@CrossOrigin(origins = "*")
@RequestMapping("/auth")
@RestController
public class AuthController {
    @Autowired
    private UserService userService;
    @PostMapping("/isUserExist/{username}")
    public boolean isUserExist(@PathVariable String userName){
        return userService.isUserExist(userName);
    }

    @PostMapping("/updateUser/{newName}/{oldName}")
    public boolean updateUser(@PathVariable String newName, @PathVariable String oldName){
        return userService.setuserName(oldName, newName);
    }
}
