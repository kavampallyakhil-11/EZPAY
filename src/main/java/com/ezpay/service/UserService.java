package com.ezpay.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ezpay.dao.UserDAO;
import com.ezpay.entity.User;

@Service
public class UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserService.class);

    private final UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // USER STORY 1.1: Registration
    @Transactional
    public String registerUser(User user) {
        logger.info("Registration request received for email: {}",
                user.getEmail());

        if (userDAO.existsByEmail(user.getEmail())) {
            logger.warn("Registration failed - email already registered");
            return "Email already registered";
        }

        if (userDAO.existsByMobile(user.getMobile())) {
            logger.warn("Registration failed - mobile already registered");
            return "Mobile number already registered";
        }

        String otp = String.valueOf(
                100000 + new java.security.SecureRandom().nextInt(900000));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        userDAO.saveUser(user);

        logger.info("User registered successfully for email: {}",
                user.getEmail());

        // Local testing only: the frontend displays this OTP in the browser.
        return "Registration successful. For local testing, your OTP is: "
                + otp;
    }

    // USER STORY 1.1: OTP verification
    @Transactional
    public String verifyOTP(String mobile, String otp) {
        logger.info("OTP verification request received");

        Optional<User> optionalUser = userDAO.findByMobile(mobile);

        if (optionalUser.isEmpty()) {
            logger.warn("OTP verification failed - user not found");
            return "User not found";
        }

        User user = optionalUser.get();

        if (user.getOtp() == null || !user.getOtp().equals(otp)) {
            logger.warn("OTP verification failed - invalid OTP");
            return "Invalid OTP";
        }

        if (user.getOtpExpiry() == null
                || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            logger.warn("OTP verification failed - OTP expired");
            return "OTP expired";
        }

        user.setOtp(null);
        user.setOtpExpiry(null);
        userDAO.saveUser(user);

        logger.info("OTP verified successfully for user ID: {}",
                user.getUserId());

        return "Account verified successfully";
    }

    // USER STORY 1.1: Login
    public User loginUser(String emailOrMobile, String password) {
        logger.info("Login request received");

        User user = userDAO.findByEmailOrMobile(emailOrMobile)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email/mobile or password"
                ));

        if (user.getOtp() != null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Please verify your OTP before logging in"
            );
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("Login failed - invalid password for user ID: {}",
                    user.getUserId());

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email/mobile or password"
            );
        }

        logger.info("Login successful for user ID: {}", user.getUserId());
        return user;
    }

    // USER STORY 1.2: Profile management
    @Transactional
    public String updateProfile(
            Long userId,
            String name,
            String address,
            String profilePicture) {

        logger.info("Profile update request received for user ID: {}", userId);

        Optional<User> optionalUser = userDAO.findById(userId);

        if (optionalUser.isEmpty()) {
            logger.warn("Profile update failed - user not found: {}", userId);
            return "User not found";
        }

        User user = optionalUser.get();

        if (name != null && !name.isBlank()) {
            user.setName(name.trim());
        }

        if (address != null) {
            user.setAddress(address.trim());
        }

        if (profilePicture != null) {
            user.setProfilePicture(profilePicture.trim());
        }

        userDAO.saveUser(user);

        logger.info("Profile updated successfully for user ID: {}", userId);
        return "Profile updated successfully";
    }

    // DASHBOARD: count of users who verified their OTP
    @Transactional(readOnly = true)
    public long getVerifiedUserCount() {
        return userDAO.countVerifiedUsers();
    }

    // DASHBOARD: list of users who verified their OTP
    @Transactional(readOnly = true)
    public List<User> getVerifiedUsers() {
        return userDAO.findVerifiedUsers();
    }

    // USER MANAGEMENT: delete a user account
    @Transactional
    public String deleteUser(Long userId) {
        logger.info("Delete request received for user ID: {}", userId);

        User user = userDAO.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        userDAO.deleteById(user.getUserId());

        logger.info("User account deleted for user ID: {}", userId);
        return "User deleted successfully";
    }

    // USER STORY 1.3: Forgot password
    @Transactional
    public String forgotPassword(String emailOrMobile) {
        logger.info("Forgot password request received");

        Optional<User> optionalUser =
                userDAO.findByEmailOrMobile(emailOrMobile);

        if (optionalUser.isEmpty()) {
            logger.warn("Forgot password request for an unregistered account");
            return "Email or mobile number not registered";
        }

        User user = optionalUser.get();
        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
        userDAO.saveUser(user);

        // Configure email or SMS delivery to send a reset link containing the token.
        logger.info("Password reset token generated for user ID: {}",
                user.getUserId());

        return "Password reset token generated. Configure email/SMS delivery to send it.";
    }

    // USER STORY 1.3: Reset password
    @Transactional
    public String resetPassword(String token, String newPassword) {
        logger.info("Password reset request received");

        Optional<User> optionalUser = userDAO.findByResetToken(token);

        if (optionalUser.isEmpty()) {
            logger.warn("Password reset failed - invalid reset token");
            return "Invalid reset token";
        }

        User user = optionalUser.get();

        if (user.getResetTokenExpiry() == null
                || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            logger.warn("Password reset failed - reset token expired");
            return "Reset token expired";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userDAO.saveUser(user);

        logger.info("Password updated successfully for user ID: {}",
                user.getUserId());

        return "Password updated successfully";
    }
}