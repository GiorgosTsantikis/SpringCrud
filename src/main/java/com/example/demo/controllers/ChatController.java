package com.example.demo.controllers;

import com.example.demo.entities.Message;
import com.example.demo.services.ImageServiceImpl;
import com.example.demo.services.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Date;
import java.util.Map;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);


    private final JwtService jwtService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ChatController(JwtService jwtService,SimpMessagingTemplate simpMessagingTemplate){
        this.jwtService=jwtService;
        this.simpMessagingTemplate=simpMessagingTemplate;
    }

    @MessageMapping("/whatever")
    @SendTo("/topic/messages")
    public Message handleChatMessage(StompHeaderAccessor accessor, String message){
        logger.debug("ChatController /chat {} sending to topic/messages",message);

        if(accessor.getSessionAttributes().get("token")!=null ) {
            logger.debug("Authenticated user sent message");
            return null;
        }
        return new Message("fucku","fucku","fucku",new Date());
    }

    @MessageMapping("/user/{id}/queue/messages")
    public void downInTheDms(Principal principal, @Payload String message, @DestinationVariable("id") String id){

        var msg= new Message(principal.getName(),id ,message,new Date());
        logger.debug("ChatController received private message from {} to {} content {} sending to queue",principal.getName(),id,message);
        simpMessagingTemplate.convertAndSendToUser(id,"/queue/messages",msg);
    }



}
