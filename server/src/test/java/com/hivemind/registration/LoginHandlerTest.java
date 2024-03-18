package com.hivemind.registration;

import com.hivemind.database.dao.UserDao;
import com.hivemind.database.models.User;
import com.hivemind.login.LoginHandler;
import com.hivemind.login.LoginStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test class for LoginHandler.
 */
public class LoginHandlerTest {

    @Mock
    private UserDao userDao;
    @InjectMocks
    private LoginHandler loginHandler;

    /**
     * Sets up the mock objects before each test method.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Tests the loginUser method for successful login.
     */
    @Test
    public void testLoginHandler_SuccessfulLogin() {
        // Arrange
        User testUser = new User(1, "testUsername", "testPassword", "test@hivemind.com");

        // Mocking the UserDao behavior
        when(userDao.getUserByUsername(anyString())).thenReturn(testUser);

        // Act
        LoginStatus loginStatus = loginHandler.loginUser(testUser);

        // Assert
        assertEquals(LoginStatus.SUCCESS, loginStatus);
        verify(userDao, times(1)).getUserByUsername("testUsername");
    }

    /**
     * Tests the loginUser method when the user is not found.
     */
    @Test
    public void testLoginHandler_UserNotFound() {
        // Arrange
        User testUser = new User(1, "nonExistingUsername", "testPassword", "test@hivemind.com");

        // Mocking the UserDao behavior
        when(userDao.getUserByUsername(anyString())).thenReturn(null);

        // Act
        LoginStatus loginStatus = loginHandler.loginUser(testUser);

        // Assert
        assertEquals(LoginStatus.USER_NOT_FOUND, loginStatus);
        verify(userDao, times(1)).getUserByUsername("nonExistingUsername");
    }

    /**
     * Tests the loginUser method for invalid password.
     */
    @Test
    public void testLoginHandler_InvalidPassword() {
        // Arrange
        User testUser = new User(1, "existingUsername", "wrongPassword", "test@hivemind.com");

        // Mocking the UserDao behavior
        when(userDao.getUserByUsername(anyString())).thenReturn(testUser);

        // Act
        LoginStatus loginStatus = loginHandler.loginUser(testUser);

        // Assert
        assertEquals(LoginStatus.INVALID_PASSWORD, loginStatus);
        verify(userDao, times(1)).getUserByUsername("existingUsername");
    }
}
