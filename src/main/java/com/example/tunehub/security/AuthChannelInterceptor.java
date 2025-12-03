package com.example.tunehub.security;
import com.example.tunehub.security.jwt.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component // חשוב שזה יהיה Bean שניתן להזרקה
public class AuthChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtUtils jwtUtils; // הקלאס שאחראי על עבודה עם JWT

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // אנחנו מתעניינים רק בהודעת החיבור הראשונית
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            // 1. קריאת כותרת Authorization מהלקוח
            List<String> authorizationHeaders = accessor.getNativeHeader("Authorization");

            if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
                String fullToken = authorizationHeaders.get(0);

                if (fullToken.startsWith("Bearer ")) {
                    String token = fullToken.substring(7); // חותך את "Bearer "

                    // 2. אימות הטוקן
                    if (jwtUtils.validateJwtToken(token)) {
                        String username = jwtUtils.getUserNameFromJwtToken(token);

                        // 3. טעינת פרטי המשתמש ויצירת אימות
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities());

                        // 4. שיתוק האימות ל-STOMP Session
                        // זהו השלב שמונע את InsufficientAuthenticationException
                        accessor.setUser(authentication);
                    }
                }
            }
        }
        return message;
    }
}