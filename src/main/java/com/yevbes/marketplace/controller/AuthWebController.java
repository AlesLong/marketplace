package com.yevbes.marketplace.controller;

import com.yevbes.marketplace.service.AuthService;
import com.yevbes.marketplace.profile.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthWebController {

    private final AuthService authService;
    private final ProfileService profileService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String name,
                           Model model) {
        try {
            String token = authService.register(email, password);
            profileService.createProfile(email, name);
            model.addAttribute("success", "User registered successfully! Please login.");
            return "auth/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        try {
            String token = authService.login(email, password);
            session.setAttribute("token", token);
            session.setAttribute("userEmail", email);
            return "redirect:/profile?email=" + email;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}