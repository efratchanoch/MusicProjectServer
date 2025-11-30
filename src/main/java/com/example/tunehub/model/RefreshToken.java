package com.example.tunehub.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // מקשר את טוקן הרענון למשתמש ספציפי
        @OneToOne
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        private Users user;

        // המחרוזת של הטוקן עצמו
        @Column(nullable = false, unique = true)
        private String token;

        // תאריך התפוגה של טוקן הרענון (התוקף הארוך, שנה)
        @Column(nullable = false)
        private Instant expiryDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }
}

