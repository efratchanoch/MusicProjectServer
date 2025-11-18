package com.example.tunehub.security;



import com.example.tunehub.security.jwt.AuthEntryPointJwt;
import com.example.tunehub.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

//הגדרות אבטחה
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Qualifier("customUserDetailsService")
    CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    public WebSecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    //********תפקיד הפונקציה:
    //מה הפונקציה מחזירה?
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //********תפקיד הפונקציה:
    //מגדירה את שרשרת מסנן האבטחה
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //משבית את הגנת CSRF על ידי הפעלת שיטת `csrf()` והשבתתה
        http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200")); // 👍 לתקן!                    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedMethods(List.of("*"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));                    corsConfiguration.setAllowCredentials(true);//לאפשר עוגיות
                    return corsConfiguration;
                }))

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                                auth.requestMatchers("/h2-console/**").permitAll()
//                                            .requestMatchers("/api/**/**").permitAll()

                                        .requestMatchers("/api/users/**").permitAll()
                                        .requestMatchers("/api/post/**").permitAll()
                                        .requestMatchers("/api/sheetMusic/**").permitAll()
                                        .requestMatchers("/api/sheetMusicCategory/**").permitAll()
                                        .requestMatchers("/api/instrument/**").permitAll()
                                        .requestMatchers("/api/users/signIn").permitAll()
                                        .requestMatchers("/api/teacher/**").permitAll()
                                        .requestMatchers(HttpMethod.POST).permitAll()
                                        .requestMatchers(HttpMethod.GET).permitAll()
                        //  .anyRequest().authenticated()
                );
        //.httpBasic(Customizer.withDefaults());

        // fix H2 database console: Refused to display ' in a frame because it set 'X-Frame-Options' to 'deny'
        http.headers(headers -> headers
                .contentSecurityPolicy(csp -> csp.policyDirectives(
                        "frame-ancestors 'self' http://localhost:4200;"
                ))
        );
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
