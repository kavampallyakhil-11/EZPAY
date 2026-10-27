package com.ezpay.dao;

import java.util.List;
import java.util.Optional;

import com.ezpay.entity.User;

public interface UserDAO {

    User saveUser(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByMobile(String mobile);

    Optional<User> findByEmailOrMobile(String emailOrMobile);

    Optional<User> findById(Long userId);

    Optional<User> findByResetToken(String resetToken);

    boolean existsByEmail(String email);

    boolean existsByMobile(String mobile);

    long countVerifiedUsers();

    List<User> findVerifiedUsers();

    void deleteById(Long userId);

	
}