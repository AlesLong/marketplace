package com.yevbes.marketplace.profile.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileResponse {
    private String userId;
    private String name;
    private String avatarUrl;
    private String phone;
    private String location;
    private String bio;
}