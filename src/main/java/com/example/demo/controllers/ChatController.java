package com.example.demo.controllers;

import com.example.demo.entities.MessageDTO;
import com.example.demo.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Map;

@Controller
public class ChatController {

    private final JwtService jwtService;

    @Autowired
    public ChatController(JwtService jwtService){
        this.jwtService=jwtService;
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public MessageDTO handleChatMessage(StompHeaderAccessor accessor,MessageDTO message){
        System.out.println(message.getContent());
        /*
        if(accessor.getSessionAttributes().get("authenticated")!=null && (boolean)accessor.getSessionAttributes().get("authenticated")) {
            System.out.println("Authenticated");
            return message;
        }
        return new MessageDTO("fucku","fucku","fucku",new Date());

         */
        return message;
    }

    @MessageMapping("/authenticate")
    public void authenticate(StompHeaderAccessor accessor,@Payload Map<String,String>  payload){
        String token=payload.get("token");
        System.out.println(token+" this is the token in /authenticate");
        boolean isValid = jwtService.isTokenValid(token);
        /*
        if (isValid) {
            accessor.getSessionAttributes().put("token", token); // Mark session as authenticated
            System.out.println("Token is valid for session: " + accessor.getSessionId());
        } else {
            accessor.getSessionAttributes().put("authenticated", false); // Mark session as unauthenticated
            System.out.println("Invalid token for session: " + accessor.getSessionId());
        }

         */
    }
}
