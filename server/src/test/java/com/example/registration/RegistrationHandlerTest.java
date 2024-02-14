package com.example.registration;

import com.example.database.models.User;
import com.example.database.dao.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test class for RegistrationHandler.
 */
public class RegistrationHandlerTest {

    @Mock
    private UserDao userDao;
    @InjectMocks
    private RegistrationHandler registrationHandler;

    /**
     * Sets up the mock objects before each test method.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Tests the registerUser method for successful registration.
     */
    @Test
    public void testRegistrationHandler_SuccessfulRegistration() {
        // Arrange
        User testUser = new User(0, "testUsername", "testPassword", "test@example.com");

        // Mocking the UserDao behavior
        when(userDao.getUserByUsername(anyString())).thenReturn(null);  // Assuming username does not exist initially
        when(userDao.getUserByEmail(anyString())).thenReturn(null); // Assuming email does not exist initially
        when(userDao.addUser(any(User.class))).thenReturn(1);   // Assuming user registration is successful

        // Act
        RegistrationStatus registrationStatus = registrationHandler.registerUser(testUser);

        // Assert
        assertEquals(RegistrationStatus.SUCCESS, registrationStatus);
        verify(userDao, times(1)).getUserByUsername("testUsername");
        verify(userDao, times(1)).getUserByEmail("test@example.com");
        verify(userDao, times(1)).addUser(testUser);
    }

    /**
     * Tests the registerUser method when the username is taken.
     */
    @Test
    public void testRegistrationHandler_UsernameTaken() {
        // Arrange
        User testUser = new User(0, "existingUsername", "testPassword", "test@example.com");

        // Mocking the UserDao behavior
        when(userDao.getUserByUsername(anyString())).thenReturn(testUser);  // Assuming username already exists

        // Act
        RegistrationStatus registrationStatus = registrationHandler.registerUser(testUser);

        // Assert
        assertEquals(RegistrationStatus.USERNAME_TAKEN, registrationStatus);
        verify(userDao, times(1)).getUserByUsername("existingUsername");
        verify(userDao, never()).getUserByEmail(anyString());
        verify(userDao, never()).addUser(any(User.class));
    }

    /**
     * Tests the registerUser method when the email is taken.
     */
    @Test
    public void testRegistrationHandler_EmailTaken() {
        // Arrange
        User testUser = new User(0, "newUsername", "testPassword", "existing@example.com");

        // Mocking the UserDao behavior
        when(userDao.getUserByUsername(anyString())).thenReturn(null);  // Assuming username does not exist initially
        when(userDao.getUserByEmail(anyString())).thenReturn(testUser); // Assuming email already exists

        // Act
        RegistrationStatus registrationStatus = registrationHandler.registerUser(testUser);

        // Assert
        assertEquals(RegistrationStatus.EMAIL_TAKEN, registrationStatus);
        verify(userDao, times(1)).getUserByUsername("newUsername");
        verify(userDao, times(1)).getUserByEmail("existing@example.com");
        verify(userDao, never()).addUser(any(User.class));
    }
}
