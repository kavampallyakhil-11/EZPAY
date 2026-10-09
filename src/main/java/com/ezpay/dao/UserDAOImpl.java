package com.ezpay.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ezpay.entity.User;
import com.ezpay.reposistory.UserRepository;

@Repository
public class UserDAOImpl implements UserDAO {

    private final UserRepository userRepository;

    public UserDAOImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.countByEmailIgnoreCase(email) > 0;
    }

    @Override
    public boolean mobileExists(String mobile) {
        return userRepository.countByMobile(mobile) > 0;
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }
}

