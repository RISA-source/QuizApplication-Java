package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import database.DBQuestion;
import models.Question;

/**
 * Test class for question CRUD operations
 */
@DisplayName("Question Management Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestQuestionManagement {

    private Question testQuestion;

    @BeforeEach
    @DisplayName("Setup - Create test question")
    void setupBeforeEach() {
        testQuestion = new Question(
            "JUnit Test Question: What is 2+2?",
            "3",
            "4",
            "5",
            "6",
            'B',
            "Beginner"
        );
    }

    @Test
    @Order(1)
    @DisplayName("Test: Add question to database")
    void testAddQuestion() {
        boolean result = DBQuestion.addQuestion(testQuestion);
        assertTrue(result, "Should successfully add question to database");
    }

    @Test
    @Order(2)
    @DisplayName("Test: Retrieve question by difficulty")
    void testGetQuestionsByDifficulty() {
        // Add test question first
        DBQuestion.addQuestion(testQuestion);
        
        // Retrieve questions
        List<Question> questions = DBQuestion.getQuestionsByDifficulty("Beginner");
        
        assertNotNull(questions, "Questions list should not be null");
        assertFalse(questions.isEmpty(), "Should have at least one Beginner question");
        
        // Verify at least one question has correct difficulty
        boolean hasBeginnerQuestion = questions.stream()
            .anyMatch(q -> q.getDifficultyLevel().equals("Beginner"));
        assertTrue(hasBeginnerQuestion, "Should have Beginner level question");
    }

    @Test
    @Order(3)
    @DisplayName("Test: Get all questions")
    void testGetAllQuestions() {
        // Add test question
        DBQuestion.addQuestion(testQuestion);
        
        List<Question> allQuestions = DBQuestion.getAllQuestions();
        
        assertNotNull(allQuestions, "Questions list should not be null");
        assertFalse(allQuestions.isEmpty(), "Should have at least one question");
    }

    @Test
    @Order(4)
    @DisplayName("Test: Update question")
    void testUpdateQuestion() {
        // Add question first
        DBQuestion.addQuestion(testQuestion);
        
        // Get the question back to get its ID
        List<Question> questions = DBQuestion.getAllQuestions();
        Question addedQuestion = questions.stream()
            .filter(q -> q.getQuestionText().contains("JUnit Test Question"))
            .findFirst()
            .orElse(null);
        
        assertNotNull(addedQuestion, "Should find the added test question");
        
        // Update the question
        addedQuestion.setQuestionText("Updated JUnit Test Question");
        addedQuestion.setCorrectOption('C');
        
        boolean updateResult = DBQuestion.updateQuestion(addedQuestion);
        assertTrue(updateResult, "Should successfully update question");
        
        // Verify update
        Question updatedQuestion = DBQuestion.getQuestionById(addedQuestion.getQuestionId());
        assertEquals("Updated JUnit Test Question", updatedQuestion.getQuestionText(), 
            "Question text should be updated");
        assertEquals('C', updatedQuestion.getCorrectOption(), 
            "Correct option should be updated");
    }

    @Test
    @Order(5)
    @DisplayName("Test: Delete question")
    void testDeleteQuestion() {
        // Add question first
        DBQuestion.addQuestion(testQuestion);
        
        // Get the question to get its ID
        List<Question> questions = DBQuestion.getAllQuestions();
        Question addedQuestion = questions.stream()
            .filter(q -> q.getQuestionText().contains("JUnit Test Question"))
            .findFirst()
            .orElse(null);
        
        assertNotNull(addedQuestion, "Should find the test question");
        
        int questionId = addedQuestion.getQuestionId();
        
        // Delete the question
        boolean deleteResult = DBQuestion.deleteQuestion(questionId);
        assertTrue(deleteResult, "Should successfully delete question");
        
        // Verify deletion
        Question deletedQuestion = DBQuestion.getQuestionById(questionId);
        assertNull(deletedQuestion, "Deleted question should not be found");
    }

    @Test
    @Order(6)
    @DisplayName("Test: Get question count")
    void testGetQuestionCount() {
        int count = DBQuestion.getTotalQuestionCount();
        assertTrue(count >= 0, "Question count should be non-negative");
    }

    @Test
    @Order(7)
    @DisplayName("Test: Get question count by difficulty")
    void testGetQuestionCountByDifficulty() {
        int beginnerCount = DBQuestion.getQuestionCountByDifficulty("Beginner");
        assertTrue(beginnerCount >= 0, "Beginner question count should be non-negative");
    }

    @Test
    @Order(8)
    @DisplayName("Test: Question object properties")
    void testQuestionObjectProperties() {
        assertEquals("JUnit Test Question: What is 2+2?", testQuestion.getQuestionText());
        assertEquals("3", testQuestion.getOptionA());
        assertEquals("4", testQuestion.getOptionB());
        assertEquals("5", testQuestion.getOptionC());
        assertEquals("6", testQuestion.getOptionD());
        assertEquals('B', testQuestion.getCorrectOption());
        assertEquals("Beginner", testQuestion.getDifficultyLevel());
        assertEquals("4", testQuestion.getCorrectAnswerText());
    }

    @AfterEach
    @DisplayName("Cleanup - Remove test questions")
    void cleanupAfterEach() {
        // Clean up any test questions
        List<Question> questions = DBQuestion.getAllQuestions();
        for (Question q : questions) {
            if (q.getQuestionText().contains("JUnit Test Question")) {
                DBQuestion.deleteQuestion(q.getQuestionId());
            }
        }
    }
}