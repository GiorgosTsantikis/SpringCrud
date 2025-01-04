package com.example.demo.config;

import com.example.demo.services.JwtService;
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

import java.util.List;
import java.util.Set;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtService jwtService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .setAllowedOrigins("http://localhost:5173")
                .addInterceptors(new HttpHandshakeInterceptor());



    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors((new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    System.out.println(accessor.toString());
                    if (accessor.containsNativeHeader("Authorization")) {
                        List<String> list = accessor.getNativeHeader("Authorization");
                        String token = list.get(0).substring(7);


                        if (jwtService.isTokenValid(token)) {
                            accessor.getSessionAttributes().put("token", token);
                            return message;
                        } else {
                           // throw new IllegalArgumentException("Invalid token.");
                            return null;

                        }
                    } else {
                        //throw new IllegalArgumentException("invalid token");
                        return null;
                    }
                } else {
                    String token = (String) accessor.getSessionAttributes().get("token");
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










