package com.yevbes.marketplace.profile.service;

import com.yevbes.marketplace.profile.dto.ProfileRequest;
import com.yevbes.marketplace.profile.dto.ProfileResponse;
import com.yevbes.marketplace.profile.entity.Profile;
import com.yevbes.marketplace.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileResponse createProfile(String userId, String name) {
        if (profileRepository.existsByUserId(userId)) {
            throw new RuntimeException("Profile already exists for user: " + userId);
        }

        Profile profile = Profile.builder()
                .userId(userId)
                .name(name != null && !name.isEmpty() ? name : "User")
                .build();

        profileRepository.save(profile);
        return mapToResponse(profile);
    }

    public ProfileResponse getProfile(String userId) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + userId));
        return mapToResponse(profile);
    }

    public ProfileResponse updateProfile(String userId, ProfileRequest request) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + userId));

        if (request.getName() != null) profile.setName(request.getName());
        if (request.getAvatarUrl() != null) profile.setAvatarUrl(request.getAvatarUrl());
        if (request.getPhone() != null) profile.setPhone(request.getPhone());
        if (request.getLocation() != null) profile.setLocation(request.getLocation());
        if (request.getBio() != null) profile.setBio(request.getBio());

        profileRepository.save(profile);
        return mapToResponse(profile);
    }

    private ProfileResponse mapToResponse(Profile profile) {
        return ProfileResponse.builder()
                .userId(profile.getUserId())
                .name(profile.getName())
                .avatarUrl(profile.getAvatarUrl())
                .phone(profile.getPhone())
                .location(profile.getLocation())
                .bio(profile.getBio())
                .build();
    }
}