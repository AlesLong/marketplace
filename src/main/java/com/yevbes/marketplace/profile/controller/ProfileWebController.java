package com.yevbes.marketplace.profile.controller;

import com.yevbes.marketplace.profile.dto.ProfileRequest;
import com.yevbes.marketplace.profile.dto.ProfileResponse;
import com.yevbes.marketplace.profile.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileWebController {

    private final ProfileService profileService;

    @GetMapping
    public String viewProfile(@RequestParam String email,
                              HttpSession session,
                              Model model) {
        // Перевіряємо чи є токен в сесії
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        try {
            ProfileResponse profile = profileService.getProfile(email);
            model.addAttribute("profile", profile);
            model.addAttribute("token", token);
            return "profile/view";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "profile/view";
        }
    }

    @GetMapping("/edit")
    public String editProfilePage(@RequestParam String email,
                                  HttpSession session,
                                  Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        try {
            ProfileResponse profile = profileService.getProfile(email);
            model.addAttribute("profile", profile);
            model.addAttribute("token", token);
            return "profile/edit";
        } catch (Exception e) {
            ProfileRequest request = new ProfileRequest();
            model.addAttribute("profile", request);
            return "profile/edit";
        }
    }

    @PostMapping("/edit")
    public String editProfile(@RequestParam String userId,
                              @RequestParam String name,
                              @RequestParam(required = false) String phone,
                              @RequestParam(required = false) String location,
                              @RequestParam(required = false) String bio,
                              HttpSession session,
                              Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        try {
            ProfileRequest request = new ProfileRequest();
            request.setName(name);
            request.setPhone(phone);
            request.setLocation(location);
            request.setBio(bio);

            ProfileResponse updated = profileService.updateProfile(userId, request);
            model.addAttribute("profile", updated);
            model.addAttribute("success", true);
            model.addAttribute("token", token);
            return "profile/edit";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "profile/edit";
        }
    }
}