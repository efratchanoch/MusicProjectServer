package com.example.tunehub.security;

import com.example.tunehub.model.Users;
import com.example.tunehub.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
public class JwtHandshakeInterceptor  {
//
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    @Autowired
//    private CustomUserDetailsService userDetailsService;
//
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request,
//                                   ServerHttpResponse response,
//                                   WebSocketHandler wsHandler,
//                                   Map<String, Object> attributes) throws Exception {
//
//        List<String> authHeaders = request.getHeaders().get("Authorization");
//        if (authHeaders != null && !authHeaders.isEmpty()) {
//            String token = authHeaders.get(0).replace("Bearer ", "");
//            if (jwtUtils.validateJwtToken(token)) {
//                String username = jwtUtils.getUserNameFromJwtToken(token);
//                Users user = userDetailsService.loadUserEntityByUsername(username);
//                attributes.put("user", user); // 🔴 נוסף: שמירת המשתמש ל-SecurityContext
//                return true;
//            }
//        }
//
//        response.setStatusCode(HttpStatus.FORBIDDEN); // 🔴 נוסף: במקרה שאין JWT תקין
//        return false;
//    }
//
//    @Override
//    public void afterHandshake(ServerHttpRequest request,
//                               ServerHttpResponse response,
//                               WebSocketHandler wsHandler,
//                               Exception exception) {}
}
