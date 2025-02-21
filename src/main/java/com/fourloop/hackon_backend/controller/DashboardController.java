package com.fourloop.hackon_backend.controller;

import com.fourloop.hackon_backend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ROLE_USER')")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getDashboardStats() {
        return ResponseEntity.ok(dashboardService.getUserProgress());
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<?> getLeaderboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dashboardService.getLeaderboard(page, size));
    }

    @GetMapping("/streak")
    public ResponseEntity<?> getStreak() {
        return ResponseEntity.ok(dashboardService.getUserStreak());
    }
}
