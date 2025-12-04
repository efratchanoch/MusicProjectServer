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

        String path = httpServletRequest.getRequestURI();

        if (path.startsWith("/api/users/signin") || path.startsWith("/api/users/signup")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        // ----------------------------------------------------

        try{

            String jwt=jwtUtils.getJwtFromCookies(httpServletRequest); // כאן אתה עדיין משתמש ב-Cookies!
            System.out.println("🚨 Filter: JWT = " + (jwt != null ? "EXISTS (length " + jwt.length() + ")" : "NULL"));
            // הלוגיקה הקיימת שלך לבדיקת הטוקן
            if(jwt !=null && jwtUtils.validateJwtToken(jwt)){
                System.out.println("🚨 Filter: Valid? " + jwtUtils.validateJwtToken(jwt));
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
            System.out.println("JWT ERROR: " + e.getMessage());
        }

        // אם הבקשה לא דולגה בצעד 1, היא תמשיך מכאן לפילטר הבא
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private String parseJwt(HttpServletRequest request) {
        // נסה לחלץ את הטוקן מכותרת ה-Authorization (Bearer Token)
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // חותך את "Bearer " (7 תווים)
        }

        // אם לא נמצא ב-Header, נסה לחלץ מה-Cookie (כפי שעשית קודם)
        return jwtUtils.getJwtFromCookies(request);
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
