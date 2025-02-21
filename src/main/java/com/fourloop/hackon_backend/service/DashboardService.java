package com.fourloop.hackon_backend.service;

import com.fourloop.hackon_backend.model.User;
import com.fourloop.hackon_backend.model.UserPrincipal;
import com.fourloop.hackon_backend.model.UserProgress;
import com.fourloop.hackon_backend.repository.UserProgressRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Map;

@Service
public class DashboardService {
    private final UserProgressRepository userProgressRepository;

    public DashboardService(UserProgressRepository userProgressRepository) {
        this.userProgressRepository = userProgressRepository;
    }

    public Map<String, Object> getUserProgress() {
        UserProgress progress = userProgressRepository.findByUser(getCurrentUser());
        return Map.of(
            "xp", progress.getXpPoints(),
            "level", progress.getLevel(),
            "achievements", progress.getAchievementsEarned(),
            "streak", progress.getCurrentStreak()
        );
    }

    public Page<UserProgress> getLeaderboard(int page, int size) {
        return userProgressRepository.findAllByOrderByXpPointsDesc(
            PageRequest.of(page, size)
        );
    }

    public Map<String, Object> getUserStreak() {
        UserProgress progress = userProgressRepository.findByUser(getCurrentUser());
        return Map.of(
            "currentStreak", progress.getCurrentStreak(),
            "lastActive", progress.getLastActiveDate()
        );
    }

    private User getCurrentUser() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
        return userPrincipal.getUser();
    }
}
