package com.fourloop.hackon_backend.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime expiryDate;


    // Constructors, Getters, and Setters

    public PasswordResetToken() {}


    public PasswordResetToken(String token, User user, LocalDateTime localDateTime) {
        this.token = token;
        this.user = user;
        this.expiryDate = localDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
