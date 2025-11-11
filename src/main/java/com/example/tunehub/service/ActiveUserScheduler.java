package com.example.tunehub.service;

import com.example.tunehub.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ActiveUserScheduler {

    @Autowired
    private UsersRepository usersRepository;



    // הגדר את "הסף" - כמה זמן נסבול שתיקה לפני שנגדיר כלא פעיל (במילישניות)
    // 3 דקות: 3 * 60 * 1000 = 180000
    private static final long INACTIVE_THRESHOLD_MS = 180000;

    @Scheduled(fixedRate = 60000) // הפעל את הבדיקה כל 60 שניות (דקה)
    public void markInactiveUsers() {
        // חישוב הזמן: כל מי שחותמת הזמן שלו ישנה מ-3 דקות אחורה
        Date threshold = new Date(System.currentTimeMillis() - INACTIVE_THRESHOLD_MS);

        // קבל את כל המשתמשים הפעילים שזמן הפעילות שלהם קודם לסף
        List<Users> inactiveUsers = usersRepository
                .findAllByLastActivityTimestampBeforeAndIsActiveIsTrue(threshold);

        if (!inactiveUsers.isEmpty()) {
            inactiveUsers.forEach(user -> user.setIsActive(false));
            usersRepository.saveAll(inactiveUsers);
            System.out.println("Marked " + inactiveUsers.size() + " users as inactive.");
        }
    }
}