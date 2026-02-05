package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;

import database.DBLogin;
import database.DBRegistration;
import database.DBQuizAttempt;
import database.DBUser;
import models.QuizAttempt;
import models.User;

/**
 * Test class for quiz attempts and scoring functionality
 */
@DisplayName("Quiz Attempt and Scoring Tests")
class TestQuizAttempt {

    private static User testUser;
    private static final String TEST_EMAIL = "quiztest@junit.test";
    private static final String TEST_USERNAME = "quizTestUser";
    private static final String TEST_PASSWORD = "test123";

    @BeforeAll
    @DisplayName("Setup - Create test user for all quiz tests")
    static void setupBeforeAll() {
        // Clean up if user exists
        User existing = DBUser.getUserByEmail(TEST_EMAIL);
        if (existing != null) {
            DBUser.deleteUser(existing.getUserId());
        }
        
        // Register test user
        DBRegistration.registerUser(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);
        
        // Login to set current user
        DBLogin.loginUser(TEST_EMAIL, TEST_PASSWORD);
        testUser = DBLogin.currentUser;
        
        System.out.println("Test user created with ID: " + testUser.getUserId());
    }

    @BeforeEach
    @DisplayName("Setup - Login test user before each test")
    void setupBeforeEach() {
        if (DBLogin.currentUser == null) {
            DBLogin.loginUser(TEST_EMAIL, TEST_PASSWORD);
            testUser = DBLogin.currentUser;
        }
    }

    @Test
    @DisplayName("Test: Save quiz attempt to database")
    void testSaveQuizAttempt() {
        QuizAttempt attempt = new QuizAttempt(
            testUser.getUserId(),
            "Beginner",
            8,
            10
        );
        
        boolean result = DBQuizAttempt.saveAttempt(attempt);
        assertTrue(result, "Should successfully save quiz attempt");
    }

    @Test
    @DisplayName("Test: Retrieve user's quiz attempts")
    void testGetUserAttempts() {
        // Save a test attempt
        QuizAttempt attempt = new QuizAttempt(testUser.getUserId(), "Beginner", 7, 10);
        DBQuizAttempt.saveAttempt(attempt);
        
        // Retrieve attempts
        List<QuizAttempt> attempts = DBQuizAttempt.getAttemptsByUser(testUser.getUserId());
        
        assertNotNull(attempts, "Attempts list should not be null");
        assertFalse(attempts.isEmpty(), "Should have at least one attempt");
        
        // Verify the attempt belongs to test user
        QuizAttempt retrieved = attempts.get(0);
        assertEquals(testUser.getUserId(), retrieved.getUserId(), 
            "Attempt should belong to test user");
    }

    @Test
    @DisplayName("Test: Calculate quiz percentage")
    void testQuizPercentageCalculation() {
        QuizAttempt attempt = new QuizAttempt(testUser.getUserId(), "Intermediate", 15, 20);
        
        double percentage = attempt.getPercentage();
        assertEquals(75.0, percentage, 0.01, "Percentage should be 75%");
    }

    @Test
    @DisplayName("Test: Get user's best score")
    void testGetUserBestScore() {
        // Save multiple attempts
        DBQuizAttempt.saveAttempt(new QuizAttempt(testUser.getUserId(), "Advanced", 5, 10));
        DBQuizAttempt.saveAttempt(new QuizAttempt(testUser.getUserId(), "Advanced", 8, 10));
        DBQuizAttempt.saveAttempt(new QuizAttempt(testUser.getUserId(), "Advanced", 6, 10));
        
        int bestScore = DBQuizAttempt.getUserBestScore(testUser.getUserId(), "Advanced");
        assertEquals(8, bestScore, "Best score should be 8");
    }

    @Test
    @DisplayName("Test: Get user's total attempts count")
    void testGetUserTotalAttempts() {
        // Save test attempts
        DBQuizAttempt.saveAttempt(new QuizAttempt(testUser.getUserId(), "Beginner", 7, 10));
        DBQuizAttempt.saveAttempt(new QuizAttempt(testUser.getUserId(), "Intermediate", 8, 10));
        
        int totalAttempts = DBQuizAttempt.getUserTotalAttempts(testUser.getUserId());
        assertTrue(totalAttempts >= 2, "Should have at least 2 attempts");
    }

    @Test
    @DisplayName("Test: Get user's average score")
    void testGetUserAverageScore() {
        // Save attempts with known scores
        DBQuizAttempt.saveAttempt(new QuizAttempt(testUser.getUserId(), "Beginner", 6, 10));
        DBQuizAttempt.saveAttempt(new QuizAttempt(testUser.getUserId(), "Beginner", 8, 10));
        
        double avgScore = DBQuizAttempt.getUserAverageScore(testUser.getUserId());
        assertTrue(avgScore >= 6.0 && avgScore <= 8.0, 
            "Average score should be between 6 and 8");
    }

    @Test
    @DisplayName("Test: Get leaderboard")
    void testGetLeaderboard() {
        // Save a high score
        DBQuizAttempt.saveAttempt(new QuizAttempt(testUser.getUserId(), "Beginner", 9, 10));
        
        List<QuizAttempt> leaderboard = DBQuizAttempt.getLeaderboard("Beginner", 10);
        
        assertNotNull(leaderboard, "Leaderboard should not be null");
        assertFalse(leaderboard.isEmpty(), "Leaderboard should have at least one entry");
        
        // Check if sorted by score (descending)
        if (leaderboard.size() > 1) {
            QuizAttempt first = leaderboard.get(0);
            QuizAttempt second = leaderboard.get(1);
            assertTrue(first.getScore() >= second.getScore(), 
                "Leaderboard should be sorted by score descending");
        }
    }

    @Test
    @DisplayName("Test: Get highest score for difficulty")
    void testGetHighestScoreForDifficulty() {
        // Save attempts with different scores
        DBQuizAttempt.saveAttempt(new QuizAttempt(testUser.getUserId(), "Intermediate", 5, 10));
        DBQuizAttempt.saveAttempt(new QuizAttempt(testUser.getUserId(), "Intermediate", 9, 10));
        DBQuizAttempt.saveAttempt(new QuizAttempt(testUser.getUserId(), "Intermediate", 7, 10));
        
        int highestScore = DBQuizAttempt.getHighestScoreForDifficulty("Intermediate");
        assertTrue(highestScore >= 9, "Highest score should be at least 9");
    }

    @Test
    @DisplayName("Test: QuizAttempt object creation and getters")
    void testQuizAttemptObject() {
        QuizAttempt attempt = new QuizAttempt(1, "Advanced", 20, 25);
        
        assertEquals(1, attempt.getUserId());
        assertEquals("Advanced", attempt.getDifficultyLevel());
        assertEquals(20, attempt.getScore());
        assertEquals(25, attempt.getTotalQuestions());
        assertEquals(80.0, attempt.getPercentage(), 0.01);
    }

    @AfterEach
    @DisplayName("Cleanup - Remove test attempts after each test")
    void cleanupAfterEach() {
        // Note: In a real scenario, you might want to keep attempts
        // For testing, we'll leave them as they use foreign keys
    }

    @AfterAll
    @DisplayName("Cleanup - Remove test user after all tests")
    static void cleanupAfterAll() {
        if (testUser != null) {
            // This will cascade delete all quiz attempts due to foreign key
            DBUser.deleteUser(testUser.getUserId());
            System.out.println("Test user deleted successfully");
        }
        DBLogin.logout();
    }
}