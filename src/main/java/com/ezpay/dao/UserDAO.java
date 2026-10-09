package com.ezpay.dao;

import java.util.List;
import java.util.Optional;

import com.ezpay.entity.User;

public interface UserDAO {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    boolean emailExists(String email);

    boolean mobileExists(String mobile);

    void delete(User user);
}