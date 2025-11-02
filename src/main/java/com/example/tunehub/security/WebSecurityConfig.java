package com.example.tunehub.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

//הגדרות אבטחה
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

        //********תפקיד הפונקציה:
        //מגדירה את שרשרת מסנן האבטחה
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            //משבית את הגנת CSRF על ידי הפעלת שיטת `csrf()` והשבתתה
            http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(request -> {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173"));
                        corsConfiguration.setAllowedMethods(List.of("*"));
                        corsConfiguration.setAllowedHeaders(List.of("*"));
                        return corsConfiguration;
                    }))

                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth ->
                                    auth.requestMatchers("/h2-console/**").permitAll()
                                            .requestMatchers("/api/**/**").permitAll()

//                  .requestMatchers("/api/user/signIn").permitAll()
                                            .anyRequest().authenticated()
                    ).httpBasic(Customizer.withDefaults());

            // fix H2 database console: Refused to display ' in a frame because it set 'X-Frame-Options' to 'deny'
            http.headers(headers -> headers.frameOptions(frameOption -> frameOption.sameOrigin()));


            return http.build();
        }


}
