package com.fourloop.hackon_backend.controller;

import com.fourloop.hackon_backend.dto.AchievementUnlockDTO;
import com.fourloop.hackon_backend.dto.ProgressUpdateDTO;
import com.fourloop.hackon_backend.dto.XpUpdateDTO;
import com.fourloop.hackon_backend.service.UserProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/api/user")
public class UserProgressController {
    private final UserProgressService userProgressService;

    public UserProgressController(UserProgressService userProgressService) {
        this.userProgressService = userProgressService;
    }

    @GetMapping("/progress")
    public ResponseEntity<?> getUserProgress() {
        return ResponseEntity.ok(userProgressService.getUserProgress());
    }

    @PostMapping("/progress")
    public ResponseEntity<?> updateUserProgress(@RequestBody ProgressUpdateDTO progress) {
        return ResponseEntity.ok(userProgressService.updateProgress(progress));
    }

    @GetMapping("/xp")
    public ResponseEntity<?> getUserXp() {
        return ResponseEntity.ok(userProgressService.getUserXpAndLevel());
    }

    @PostMapping("/xp")
    public ResponseEntity<?> addUserXp(@RequestBody XpUpdateDTO xpUpdate) {
        return ResponseEntity.ok(userProgressService.addXp(xpUpdate));
    }

    @GetMapping("/next-level")
    public ResponseEntity<?> getNextLevelRequirements() {
        return ResponseEntity.ok(userProgressService.getNextLevelRequirements());
    }

    @GetMapping("/achievements")
    public ResponseEntity<?> getUserAchievements() {
        return ResponseEntity.ok(userProgressService.getUserAchievements());
    }

    @PostMapping("/achievements")
    public ResponseEntity<?> unlockAchievement(@RequestBody AchievementUnlockDTO achievement) {
        return ResponseEntity.ok(userProgressService.unlockAchievement(achievement));
    }

    @GetMapping("/streak")
    public ResponseEntity<?> getUserStreak() {
        return ResponseEntity.ok(userProgressService.getUserStreak());
    }

    @PostMapping("/streak")
    public ResponseEntity<?> updateStreak() {
        return ResponseEntity.ok(userProgressService.updateStreak());
    }
}
