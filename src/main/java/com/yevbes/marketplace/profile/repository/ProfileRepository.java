package com.yevbes.marketplace.profile.repository;

import com.yevbes.marketplace.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserId(String userId);
    boolean existsByUserId(String userId);
}
