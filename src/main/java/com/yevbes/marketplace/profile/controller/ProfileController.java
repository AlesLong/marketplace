package com.yevbes.marketplace.profile.controller;

import com.yevbes.marketplace.profile.dto.ProfileRequest;
import com.yevbes.marketplace.profile.dto.ProfileResponse;
import com.yevbes.marketplace.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/{userId}")
    public ResponseEntity<ProfileResponse> createProfile(
            @PathVariable String userId,
            @RequestParam(required = false) String name) {
        return ResponseEntity.ok(profileService.createProfile(userId, name));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable String userId) {
        return ResponseEntity.ok(profileService.getProfile(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable String userId,
            @RequestBody ProfileRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(userId, request));
    }
}