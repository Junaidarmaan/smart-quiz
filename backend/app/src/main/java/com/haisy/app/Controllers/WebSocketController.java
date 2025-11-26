package com.haisy.app.Controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @MessageMapping("/request")
    public void handle(){
        //"hello world";
    }
}
