package com.ezpay.reposistory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezpay.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByMobile(String mobile);

    Optional<User> findByResetToken(String resetToken);

    long countByEmail(String email);

    long countByMobile(String mobile);

    long countByOtpIsNull();

    List<User> findAllByOtpIsNull();
}