package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import database.DBLogin;
import database.DBRegistration;
import database.DBUser;
import models.User;

/**
 * Test class for user registration and login functionality
 */
@DisplayName("User Registration and Login Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestUserAuthentication {

    private static final String TEST_USERNAME = "testuser_junit";
    private static final String TEST_EMAIL = "testuser@junit.test";
    private static final String TEST_PASSWORD = "test123";

    @BeforeEach
    @DisplayName("Setup - Clean up test user before each test")
    void setupBeforeEach() {
        // Clean up test user if exists
        User existingUser = DBUser.getUserByEmail(TEST_EMAIL);
        if (existingUser != null) {
            DBUser.deleteUser(existingUser.getUserId());
        }
    }

    @Test
    @Order(1)
    @DisplayName("Test: User registration with valid data")
    void testUserRegistrationSuccess() {
        boolean result = DBRegistration.registerUser(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        assertTrue(result, "User registration should succeed with valid data");
    }

    @Test
    @Order(2)
    @DisplayName("Test: Cannot register duplicate email")
    void testDuplicateEmailRegistration() {
        // First registration
        DBRegistration.registerUser(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        
        // Try to register again with same email
        boolean result = DBRegistration.registerUser("anotheruser", TEST_EMAIL, "password456");
        assertFalse(result, "Should not be able to register with duplicate email");
    }

    @Test
    @Order(3)
    @DisplayName("Test: Login with correct credentials")
    void testLoginSuccess() {
        // Register user first
        DBRegistration.registerUser(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        
        // Try to login
        boolean loginResult = DBLogin.loginUser(TEST_EMAIL, TEST_PASSWORD);
        assertTrue(loginResult, "Login should succeed with correct credentials");
        
        // Verify currentUser is set
        assertNotNull(DBLogin.currentUser, "Current user should be set after successful login");
        assertEquals(TEST_EMAIL, DBLogin.currentUser.getEmail(), "Logged in user email should match");
    }

    @Test
    @Order(4)
    @DisplayName("Test: Login fails with wrong password")
    void testLoginWrongPassword() {
        // Register user first
        DBRegistration.registerUser(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        
        // Try to login with wrong password
        boolean loginResult = DBLogin.loginUser(TEST_EMAIL, "wrongpassword");
        assertFalse(loginResult, "Login should fail with wrong password");
    }

    @Test
    @Order(5)
    @DisplayName("Test: Login fails with non-existent email")
    void testLoginNonExistentUser() {
        boolean loginResult = DBLogin.loginUser("nonexistent@test.com", "password");
        assertFalse(loginResult, "Login should fail for non-existent user");
    }

    @Test
    @Order(6)
    @DisplayName("Test: Logout clears current user")
    void testLogout() {
        // Register and login
        DBRegistration.registerUser(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        DBLogin.loginUser(TEST_EMAIL, TEST_PASSWORD);
        
        assertNotNull(DBLogin.currentUser, "User should be logged in");
        
        // Logout
        DBLogin.logout();
        
        assertNull(DBLogin.currentUser, "Current user should be null after logout");
    }

    @AfterEach
    @DisplayName("Cleanup - Remove test user after each test")
    void cleanupAfterEach() {
        // Clean up test user
        User testUser = DBUser.getUserByEmail(TEST_EMAIL);
        if (testUser != null) {
            DBUser.deleteUser(testUser.getUserId());
        }
        
        // Clear logged in user
        DBLogin.logout();
    }
}