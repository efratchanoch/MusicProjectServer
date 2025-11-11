package com.example.tunehub.security.jwt;

import com.example.tunehub.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Override

    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        // 1. 🔑 הוספת לוגיקת הדילוג (התיקון העיקרי ל-403)
        String path = httpServletRequest.getRequestURI();

        // אם הנתיב הוא Login, Sign Up או Sign Out, דלג על בדיקת ה-JWT
        if (path.startsWith("/api/users/signin") || path.startsWith("/api/users/signup")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        // ----------------------------------------------------

        try{
            String jwt=jwtUtils.getJwtFromCookies(httpServletRequest); // כאן אתה עדיין משתמש ב-Cookies!

            // הלוגיקה הקיימת שלך לבדיקת הטוקן
            if(jwt !=null && jwtUtils.validateJwtToken(jwt)){
                String userName=jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails= userDetailsService.loadUserByUsername(userName);

                UsernamePasswordAuthenticationToken authentication=
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }

        }
        catch (Exception e)
        {
            // ⚠️ מומלץ לא רק להדפיס, אלא לשלוח קוד שגיאה 401 אם אימות נכשל.
            // כרגע נשאיר את ההדפסה כפי שהיא, אך ה-403 נפתר על ידי הדילוג.
            System.out.println(e);
        }

        // אם הבקשה לא דולגה בצעד 1, היא תמשיך מכאן לפילטר הבא
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
//    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        try{
//            String jwt=jwtUtils.getJwtFromCookies(httpServletRequest);
//            //*********מהי השאלה כאן???
//            if(jwt !=null && jwtUtils.validateJwtToken(jwt)){
//                String userName=jwtUtils.getUserNameFromJwtToken(jwt);
//                UserDetails userDetails= userDetailsService.loadUserByUsername(userName);
//
//                UsernamePasswordAuthenticationToken authentication=
//                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            }
//
//        }
//        catch (Exception e)
//        {
//            System.out.println(e);
//        }
//        //***************מה משמעות ה-filter??
//        filterChain.doFilter(httpServletRequest,httpServletResponse);
//    }

}
