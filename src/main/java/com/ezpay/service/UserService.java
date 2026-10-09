package com.ezpay.service;

import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ezpay.dao.UserDAO;
import com.ezpay.entity.User;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional
    public User register(User user) {
        String email = normalizeEmail(user.getEmail());
        String mobile = user.getMobile().trim();

        if (userDAO.emailExists(email)) {
            logger.warn("Registration rejected: email already exists");
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Email is already registered");
        }

        if (userDAO.mobileExists(mobile)) {
            logger.warn("Registration rejected: mobile already exists");
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Mobile number is already registered");
        }

        user.setId(null);
        user.setFullName(user.getFullName().trim());
        user.setEmail(email);
        user.setMobile(mobile);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userDAO.save(user);
        logger.info("User registered; userId={}", savedUser.getId());

        return savedUser;
    }

    public User login(String email, String rawPassword) {
        User user = userDAO.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> {
                    logger.warn("Login failed: account not found");
                    return new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "Invalid email or password");
                });

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            logger.warn("Login failed: password mismatch; userId={}", user.getId());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password");
        }

        logger.info("Login credentials validated; userId={}", user.getId());
        return user;
    }

    public User getUser(Long id) {
        return userDAO.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found; userId={}", id);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "User not found");
                });
    }

    public List<User> getAllUsers() {
        logger.info("Retrieving users");
        return userDAO.findAll();
    }

    @Transactional
    public User updateProfile(Long id, User request) {
        User existingUser = getUser(id);
        String newMobile = request.getMobile().trim();

        if (!existingUser.getMobile().equals(newMobile)
                && userDAO.mobileExists(newMobile)) {
            logger.warn("Profile update rejected: mobile already exists; userId={}", id);
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Mobile number is already registered");
        }

        existingUser.setFullName(request.getFullName().trim());
        existingUser.setMobile(newMobile);

        User updatedUser = userDAO.save(existingUser);
        logger.info("Profile updated; userId={}", id);

        return updatedUser;
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email is required");
        }

        return email.trim().toLowerCase(Locale.ROOT);
    }
}