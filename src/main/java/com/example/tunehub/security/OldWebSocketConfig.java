package com.example.tunehub.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class OldWebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Autowired
//    private JwtHandshakeInterceptor jwtHandshakeInterceptor; // 🔴 נוסף
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws-notifications")
//                .setAllowedOriginPatterns("*") // 🔴 שונה: מתאים ל-CORS עם credentials
//                .withSockJS()
//                .setInterceptors(jwtHandshakeInterceptor); // 🔴 נוסף: interceptor ל-JWT
//    }
//
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/topic", "/queue");
//        registry.setApplicationDestinationPrefixes("/app");
//        registry.setUserDestinationPrefix("/user"); // **חשוב לשליחת הודעות ל-user ספציפי**
//
//    }

}
