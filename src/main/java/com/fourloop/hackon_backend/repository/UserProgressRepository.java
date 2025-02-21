package com.fourloop.hackon_backend.repository;

import com.fourloop.hackon_backend.model.User;
import com.fourloop.hackon_backend.model.UserProgress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    UserProgress findByUser(User user);
    Page<UserProgress> findAllByOrderByXpPointsDesc(Pageable pageable);
}
