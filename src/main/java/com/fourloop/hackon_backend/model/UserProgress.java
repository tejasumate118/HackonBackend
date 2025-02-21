package com.fourloop.hackon_backend.model;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_progress")
public class UserProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;
    
    private int xpPoints;
    private int level;
    private int currentStreak;
    private int longestStreak;
    private String lastActiveDate;
    private int problemsSolved;
    private int achievementsEarned;

    @ElementCollection
    @CollectionTable(name = "module_progress", 
        joinColumns = @JoinColumn(name = "user_progress_id"))
    @MapKeyColumn(name = "module_id")
    @Column(name = "progress")
    private Map<String, Integer> moduleProgress = new HashMap<>();

    private List<String> achievements;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getXpPoints() {
        return xpPoints;
    }

    public void setXpPoints(int xpPoints) {
        this.xpPoints = xpPoints;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(int longestStreak) {
        this.longestStreak = longestStreak;
    }

    public String getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(String lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public int getProblemsSolved() {
        return problemsSolved;
    }

    public void setProblemsSolved(int problemsSolved) {
        this.problemsSolved = problemsSolved;
    }

    public int getAchievementsEarned() {
        return achievementsEarned;
    }

    public void setAchievementsEarned(int achievementsEarned) {
        this.achievementsEarned = achievementsEarned;
    }

    public void setModuleProgress(String moduleId, int progress) {
        if (progress < 0) progress = 0;
        if (progress > 100) progress = 100;
        moduleProgress.put(moduleId, progress);
    }

    public Map<String, Integer> getModuleProgress() {
        return moduleProgress;
    }

    public void addAchievement(String achievementId) {
        if (this.achievements == null) {
            this.achievements = new ArrayList<>();
        }
        if (!this.achievements.contains(achievementId)) {
            this.achievements.add(achievementId);
        }
    }

    public List<String> getAchievements() {
        if (this.achievements == null) {
            this.achievements = new ArrayList<>();
        }
        return this.achievements;
    }
}
