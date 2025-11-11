package com.example.tunehub.config; // 👈 ודא ששם החבילה תואם למה שיצרת

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // הנתיב הפיזי במחשב שלך שבו נשמרים הקבצים
    // אנו משתמשים בלוכסן קדמי (/) כדי להיות תואמים לכל מערכות ההפעלה
    private static final String UPLOAD_LOCATION =
            "file:" + System.getProperty("user.dir") + "/media/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 1. הנתיב הציבורי ב-URL: /images/
        // 2. המיפוי לנתיב הפנימי: UPLOAD_LOCATION
        registry.addResourceHandler("/images/**")
                .addResourceLocations(UPLOAD_LOCATION);
    }
}