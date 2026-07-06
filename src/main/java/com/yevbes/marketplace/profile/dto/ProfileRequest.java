package com.yevbes.marketplace.profile.dto;

import lombok.Data;

@Data
public class ProfileRequest {
    private String name;
    private String avatarUrl;
    private String phone;
    private String location;
    private String bio;
}