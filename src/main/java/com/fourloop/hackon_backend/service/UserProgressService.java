package com.fourloop.hackon_backend.service;

import com.fourloop.hackon_backend.dto.AchievementUnlockDTO;
import com.fourloop.hackon_backend.dto.ProgressUpdateDTO;
import com.fourloop.hackon_backend.dto.XpUpdateDTO;
import com.fourloop.hackon_backend.model.User;
import com.fourloop.hackon_backend.model.UserPrincipal;
import com.fourloop.hackon_backend.model.UserProgress;
import com.fourloop.hackon_backend.repository.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserProgressService {
    private final UserProgressRepository userProgressRepository;
    private final UserService userService;

    public UserProgressService(UserProgressRepository userProgressRepository, UserService userService) {
        this.userProgressRepository = userProgressRepository;
        this.userService = userService;
    }

    public UserProgress getUserProgress() {
        User currentUser = userService.getCurrentUser();
        return userProgressRepository.findByUser(currentUser);
    }

    public UserProgress updateProgress(ProgressUpdateDTO progress) {
        UserProgress userProgress = getUserProgress();
        userProgress.setModuleProgress(progress.getModuleId(), progress.getProgress());
        return userProgressRepository.save(userProgress);
    }

    public Map<String, Integer> getUserXpAndLevel() {
        UserProgress progress = getUserProgress();
        Map<String, Integer> xpInfo = new HashMap<>();
        xpInfo.put("xp", progress.getXpPoints());
        xpInfo.put("level", progress.getLevel());
        return xpInfo;
    }

    public UserProgress addXp(XpUpdateDTO xpUpdate) {
        UserProgress progress = getUserProgress();
        int newXp = progress.getXpPoints() + xpUpdate.getXpAmount();
        progress.setXpPoints(newXp);
        
        // Level up logic (100 XP per level)
        int newLevel = newXp / 100;
        if (newLevel > progress.getLevel()) {
            progress.setLevel(newLevel);
        }
        
        return userProgressRepository.save(progress);
    }

    public Map<String, Integer> getNextLevelRequirements() {
        UserProgress progress = getUserProgress();
        Map<String, Integer> requirements = new HashMap<>();
        int currentLevel = progress.getLevel();
        int xpForNextLevel = (currentLevel + 1) * 100;
        requirements.put("xpNeeded", xpForNextLevel - progress.getXpPoints());
        requirements.put("nextLevel", currentLevel + 1);
        return requirements;
    }

    public List<String> getUserAchievements() {
        return getUserProgress().getAchievements();
    }

    public UserProgress unlockAchievement(AchievementUnlockDTO achievement) {
        UserProgress progress = getUserProgress();
        progress.addAchievement(achievement.getAchievementId());
        progress.setAchievementsEarned(progress.getAchievements().size());
        return userProgressRepository.save(progress);
    }

    public Map<String, Integer> getUserStreak() {
        UserProgress progress = getUserProgress();
        Map<String, Integer> streakInfo = new HashMap<>();
        streakInfo.put("current", progress.getCurrentStreak());
        streakInfo.put("longest", progress.getLongestStreak());
        return streakInfo;
    }

    public UserProgress updateStreak() {
        UserProgress progress = getUserProgress();
        LocalDate today = LocalDate.now();
        LocalDate lastActive = LocalDate.parse(progress.getLastActiveDate());
        
        if (lastActive.plusDays(1).equals(today)) {
            progress.setCurrentStreak(progress.getCurrentStreak() + 1);
            if (progress.getCurrentStreak() > progress.getLongestStreak()) {
                progress.setLongestStreak(progress.getCurrentStreak());
            }
        } else if (!lastActive.equals(today)) {
            progress.setCurrentStreak(1);
        }
        
        progress.setLastActiveDate(today.toString());
        return userProgressRepository.save(progress);
    }

    private User getCurrentUser() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
        return userPrincipal.getUser();
    }

    private int calculateLevel(int xp) {
        // Simple level calculation: level = sqrt(xp/100)
        return (int) Math.floor(Math.sqrt(xp / 100.0));
    }

    private int calculateXpToNextLevel(int currentLevel) {
        int nextLevel = currentLevel + 1;
        return (nextLevel * nextLevel) * 100; // XP needed for next level
    }
}
