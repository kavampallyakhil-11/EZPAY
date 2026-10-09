package com.ezpay.reposistory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezpay.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    long countByEmailIgnoreCase(String email);

    long countByMobile(String mobile);
}