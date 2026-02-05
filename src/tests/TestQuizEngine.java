package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import logic.QuizEngine;
import models.Question;

/**
 * Test class for QuizEngine game logic
 * Note: Requires at least 25 questions in database for selected difficulty
 */
@DisplayName("Quiz Engine Logic Tests")
class TestQuizEngine {

    private QuizEngine quizEngine;
    private static final String TEST_DIFFICULTY = "Beginner";

    @BeforeEach
    @DisplayName("Setup - Create quiz engine instance")
    void setupBeforeEach() {
        quizEngine = new QuizEngine(TEST_DIFFICULTY);
    }

    @Test
    @DisplayName("Test: Quiz engine initialization")
    void testQuizEngineInitialization() {
        assertNotNull(quizEngine, "Quiz engine should be initialized");
        assertEquals(TEST_DIFFICULTY, quizEngine.getDifficultyLevel(), 
            "Difficulty should match initialization parameter");
    }

    @Test
    @DisplayName("Test: Start game successfully")
    void testStartGame() {
        boolean started = quizEngine.startGame();
        // This will pass if database has at least 25 Beginner questions
        // If it fails, you need to add more questions to the database
        assertTrue(started, "Game should start successfully (need 25+ questions in DB)");
    }

    @Test
    @DisplayName("Test: Get current question after starting game")
    void testGetCurrentQuestion() {
        quizEngine.startGame();
        Question currentQuestion = quizEngine.getCurrentQuestion();
        
        assertNotNull(currentQuestion, "Should have a current question after starting game");
        assertNotNull(currentQuestion.getQuestionText(), "Question text should not be null");
        assertNotNull(currentQuestion.getOptionA(), "Option A should not be null");
        assertNotNull(currentQuestion.getOptionB(), "Option B should not be null");
        assertNotNull(currentQuestion.getOptionC(), "Option C should not be null");
        assertNotNull(currentQuestion.getOptionD(), "Option D should not be null");
    }

    @Test
    @DisplayName("Test: Total rounds is 5")
    void testTotalRounds() {
        assertEquals(5, quizEngine.getTotalRounds(), "Should have 5 total rounds");
    }

    @Test
    @DisplayName("Test: Questions per round is 5")
    void testQuestionsPerRound() {
        assertEquals(5, quizEngine.getQuestionsPerRound(), "Should have 5 questions per round");
    }

    @Test
    @DisplayName("Test: Current round number starts at 1")
    void testCurrentRoundNumber() {
        quizEngine.startGame();
        assertEquals(1, quizEngine.getCurrentRoundNumber(), 
            "Current round should start at 1");
    }

    @Test
    @DisplayName("Test: Current question number starts at 1")
    void testCurrentQuestionNumber() {
        quizEngine.startGame();
        assertEquals(1, quizEngine.getCurrentQuestionNumber(), 
            "Current question should start at 1");
    }

    @Test
    @DisplayName("Test: Submit answer")
    void testSubmitAnswer() {
        quizEngine.startGame();
        
        // Submit an answer (doesn't matter if correct for this test)
        assertDoesNotThrow(() -> quizEngine.submitAnswer('A'), 
            "Should be able to submit answer without exception");
    }

    @Test
    @DisplayName("Test: Move to next question")
    void testNextQuestion() {
        quizEngine.startGame();
        
        int initialQuestion = quizEngine.getCurrentQuestionNumber();
        quizEngine.submitAnswer('A');
        boolean hasNext = quizEngine.nextQuestion();
        
        if (hasNext) {
            assertEquals(initialQuestion + 1, quizEngine.getCurrentQuestionNumber(), 
                "Question number should increment");
        }
    }

    @Test
    @DisplayName("Test: Complete one round")
    void testCompleteRound() {
        quizEngine.startGame();
        
        // Answer all 5 questions in round 1
        for (int i = 0; i < 5; i++) {
            quizEngine.submitAnswer('A'); // Just submit any answer
            if (i < 4) { // Don't call next after last question
                quizEngine.nextQuestion();
            }
        }
        
        // Finish the round
        QuizEngine.RoundResult result = quizEngine.finishRound();
        
        assertNotNull(result, "Round result should not be null");
        assertEquals(1, result.roundNumber, "Should be round 1");
        assertEquals(5, result.totalQuestions, "Round should have 5 questions");
        assertTrue(result.score >= 0 && result.score <= 5, 
            "Score should be between 0 and 5");
    }

    @Test
    @DisplayName("Test: Round result percentage calculation")
    void testRoundResultPercentage() {
        quizEngine.startGame();
        
        // Get correct answer for first question
        Question q = quizEngine.getCurrentQuestion();
        char correctAnswer = q.getCorrectOption();
        
        // Answer all questions with correct answer from first question
        for (int i = 0; i < 5; i++) {
            quizEngine.submitAnswer(correctAnswer);
            if (i < 4) {
                quizEngine.nextQuestion();
            }
        }
        
        QuizEngine.RoundResult result = quizEngine.finishRound();
        
        // At least the first answer should be correct
        assertTrue(result.score >= 1, "Should have at least 1 correct answer");
        assertTrue(result.percentage >= 0 && result.percentage <= 100, 
            "Percentage should be between 0 and 100");
    }

    @Test
    @DisplayName("Test: Move to next round")
    void testNextRound() {
        quizEngine.startGame();
        
        // Complete round 1
        for (int i = 0; i < 5; i++) {
            quizEngine.submitAnswer('A');
            if (i < 4) quizEngine.nextQuestion();
        }
        quizEngine.finishRound();
        
        // Move to round 2
        boolean hasNextRound = quizEngine.nextRound();
        
        assertTrue(hasNextRound, "Should have next round (round 2)");
        assertEquals(2, quizEngine.getCurrentRoundNumber(), 
            "Should now be on round 2");
        assertEquals(1, quizEngine.getCurrentQuestionNumber(), 
            "Question number should reset to 1 in new round");
    }

    @Test
    @DisplayName("Test: Game is active after starting")
    void testGameIsActive() {
        quizEngine.startGame();
        assertTrue(quizEngine.isActive(), "Game should be active after starting");
    }

    @Test
    @DisplayName("Test: Previous question navigation")
    void testPreviousQuestion() {
        quizEngine.startGame();
        
        // Move to question 2
        quizEngine.submitAnswer('A');
        quizEngine.nextQuestion();
        
        assertEquals(2, quizEngine.getCurrentQuestionNumber());
        
        // Go back to question 1
        boolean hasPrevious = quizEngine.previousQuestion();
        
        assertTrue(hasPrevious, "Should be able to go to previous question");
        assertEquals(1, quizEngine.getCurrentQuestionNumber(), 
            "Should be back at question 1");
    }

    @Test
    @DisplayName("Test: Cannot go to previous from first question")
    void testCannotGoPreviousFromFirst() {
        quizEngine.startGame();
        
        boolean hasPrevious = quizEngine.previousQuestion();
        
        assertFalse(hasPrevious, "Should not be able to go previous from first question");
        assertEquals(1, quizEngine.getCurrentQuestionNumber(), 
            "Should still be at question 1");
    }

    @AfterEach
    @DisplayName("Cleanup - Reset quiz engine")
    void cleanupAfterEach() {
        quizEngine = null;
    }
}