package com.ezpay.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezpay.entity.User;
import com.ezpay.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/verify-otp")
    public String verifyOTP(@RequestBody User user) {
        return userService.verifyOTP(user.getMobile(), user.getOtp());
    }

    @PostMapping("/login")
    public Map<String, Object> loginUser(@RequestBody User user) {
        User loggedInUser = userService.loginUser(
                user.getEmail(),
                user.getPassword()
        );

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("userId", loggedInUser.getUserId());
        response.put("name", loggedInUser.getName());
        response.put("email", loggedInUser.getEmail());
        response.put("mobile", loggedInUser.getMobile());

        return response;
    }

    @GetMapping("/count")
    public Map<String, Long> getVerifiedUserCount() {
        return Map.of(
                "verifiedUserCount",
                userService.getVerifiedUserCount()
        );
    }

    @GetMapping("/verified")
    public List<Map<String, Object>> getVerifiedUsers() {
        List<Map<String, Object>> response = new ArrayList<>();

        for (User user : userService.getVerifiedUsers()) {
            Map<String, Object> account = new LinkedHashMap<>();
            account.put("userId", user.getUserId());
            account.put("name", user.getName());
            account.put("email", user.getEmail());
            account.put("mobile", user.getMobile());
            response.add(account);
        }

        return response;
    }

    @PutMapping("/{userId}/profile")
    public String updateProfile(
            @PathVariable Long userId,
            @RequestBody User user) {

        return userService.updateProfile(
                userId,
                user.getName(),
                user.getAddress(),
                user.getProfilePicture()
        );
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody User user) {
        return userService.forgotPassword(user.getEmail());
    }

    @PostMapping("/reset-password/{token}")
    public String resetPassword(
            @PathVariable String token,
            @RequestBody User user) {

        return userService.resetPassword(token, user.getPassword());
    }
}