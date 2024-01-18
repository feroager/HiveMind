package com.example.registration;

import com.example.database.models.User;
import com.example.login.LoginHandler;
import com.example.login.LoginStatus;
import com.example.database.dao.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LoginHandlerTest {

    @Mock
    private UserDao userDao;
    @InjectMocks
    private LoginHandler loginHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoginHandler_SuccessfulLogin() {
        // Arrange
        User testUser = new User(1, "testUsername", "testPassword", "test@example.com");

        // Mocking the UserDao behavior
        when(userDao.getUserByUsername(anyString())).thenReturn(testUser);

        // Act
        LoginStatus loginStatus = loginHandler.loginUser(testUser);

        // Assert
        assertEquals(LoginStatus.SUCCESS, loginStatus);
        verify(userDao, times(1)).getUserByUsername("testUsername");
    }

    @Test
    public void testLoginHandler_UserNotFound() {
        // Arrange
        User testUser = new User(1, "nonExistingUsername", "testPassword", "test@example.com");

        // Mocking the UserDao behavior
        when(userDao.getUserByUsername(anyString())).thenReturn(null);

        // Act
        LoginStatus loginStatus = loginHandler.loginUser(testUser);

        // Assert
        assertEquals(LoginStatus.USER_NOT_FOUND, loginStatus);
        verify(userDao, times(1)).getUserByUsername("nonExistingUsername");
    }

    @Test
    public void testLoginHandler_InvalidPassword() {
        // Arrange
        User testUser = new User(1, "existingUsername", "wrongPassword", "test@example.com");

        // Mocking the UserDao behavior
        when(userDao.getUserByUsername(anyString())).thenReturn(testUser);

        // Act
        LoginStatus loginStatus = loginHandler.loginUser(testUser);

        // Assert
        assertEquals(LoginStatus.INVALID_PASSWORD, loginStatus);
        verify(userDao, times(1)).getUserByUsername("existingUsername");
    }
}
