package com.example.demo.config;

import com.example.demo.services.ImageServiceImpl;
import com.example.demo.services.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;
import java.util.List;
import java.util.Set;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .setAllowedOrigins("http://localhost:5173")
                .addInterceptors(new HttpHandshakeInterceptor());



    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic","/user");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors((new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                logger.debug("received message {}, channel {}",message,channel);
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    if (accessor.containsNativeHeader("Authorization")) {
                        List<String> list = accessor.getNativeHeader("Authorization");
                        String token = list.get(0).substring(7);
                        logger.debug("STOMP CONNECT COMMAND TOKEN {}",token);

                        if (token!=null && jwtService.isTokenValid(token)) {
                            accessor.getSessionAttributes().put("token", token);
                            logger.debug("STOMP CONNECT COMMAND TOKEN VALID");
                            accessor.setUser(new Principal() {
                                @Override
                                public String getName() {
                                    String id= jwtService.getIdFromToken(token);
                                    logger.debug("id from token {}",token);
                                    return id;
                                }
                            });
                            return message;
                        } else {
                           // throw new IllegalArgumentException("Invalid token.");
                            logger.warn("STOMP CONNECT COMMAND TOKEN INVALID");
                            return null;

                        }
                    } else {
                        logger.warn("STOMP CONNECT COMMAND NO AUTHORIZATION HEADER");
                        //throw new IllegalArgumentException("invalid token");
                        return null;
                    }
                } else {

                    String token = (String) accessor.getSessionAttributes().get("token");
                    logger.debug("STOMP {} COMMAND, TOKEN {}",accessor.getCommand(),token);
                    if (jwtService.validateToken(token)) {
                        return message;
                    } else {
                        //throw new IllegalArgumentException("Invalid token.");
                        return null;
                    }
                }
            }
        }));

    }
}










