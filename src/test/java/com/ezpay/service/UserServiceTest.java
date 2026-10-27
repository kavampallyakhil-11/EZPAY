package com.ezpay.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.ezpay.dao.UserDAO;
import com.ezpay.entity.User;

class UserServiceTest {

    private UserDAO userDAO;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        userService = new UserService(userDAO);
    }

    @Test
    void deleteUser_whenUserExists_deletesAndReturnsSuccess() {
        Long userId = 7L;
        User user = new User();
        user.setUserId(userId);

        when(userDAO.findById(userId)).thenReturn(Optional.of(user));

        String result = userService.deleteUser(userId);

        assertEquals("User deleted successfully", result);
        verify(userDAO).deleteById(userId);
    }

    @Test
    void deleteUser_whenUserDoesNotExist_throwsNotFound() {
        Long userId = 999L;

        when(userDAO.findById(userId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> userService.deleteUser(userId)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(userDAO, never()).deleteById(userId);
    }
}