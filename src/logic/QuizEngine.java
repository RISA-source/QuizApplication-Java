package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import models.Question;
import models.QuizAttempt;
import database.DBQuestion;
import database.DBQuizAttempt;
import database.DBLogin;

/**
 * Quiz Engine - Handles 5-round quiz game logic
 * Each game consists of 5 rounds, each round has questions
 */
public class QuizEngine {
    
    // Game configuration
    private static final int TOTAL_ROUNDS = 5;
    private static final int QUESTIONS_PER_ROUND = 5; // 5 questions per round
    
    // Game state
    private String difficultyLevel;
    private int currentRound;                    // Current round (0-4)
    private List<RoundData> rounds;              // Data for all 5 rounds
    private boolean isGameActive;
    
    // Current round state
    private List<Question> currentRoundQuestions;
    private List<Character> currentRoundAnswers;
    private int currentQuestionIndex;
    
    /**
     * Constructor - Initialize a new game session
     * @param difficultyLevel The difficulty level for all rounds
     */
    public QuizEngine(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
        this.currentRound = 0;
        this.rounds = new ArrayList<>();
        this.isGameActive = false;
        this.currentQuestionIndex = 0;
    }
    
    /**
     * Start the game - Initialize all 5 rounds
     * @return true if game started successfully, false if not enough questions
     */
    public boolean startGame() {
        // Load all questions for this difficulty
        List<Question> allQuestions = DBQuestion.getQuestionsByDifficulty(difficultyLevel);
        
        // Check if we have enough questions for 5 rounds
        int questionsNeeded = TOTAL_ROUNDS * QUESTIONS_PER_ROUND;
        if (allQuestions.size() < questionsNeeded) {
            return false; // Not enough questions
        }
        
        // Shuffle all questions
        Collections.shuffle(allQuestions);
        
        // Distribute questions into 5 rounds
        for (int round = 0; round < TOTAL_ROUNDS; round++) {
            int startIndex = round * QUESTIONS_PER_ROUND;
            int endIndex = startIndex + QUESTIONS_PER_ROUND;
            
            List<Question> roundQuestions = new ArrayList<>(
                allQuestions.subList(startIndex, endIndex)
            );
            
            // Shuffle options for each question in this round
            for (Question q : roundQuestions) {
                shuffleQuestionOptions(q);
            }
            
            rounds.add(new RoundData(roundQuestions));
        }
        
        // Start first round
        currentRound = 0;
        startCurrentRound();
        isGameActive = true;
        
        return true;
    }
    
    /**
     * Start the current round
     */
    private void startCurrentRound() {
        if (currentRound < rounds.size()) {
            RoundData round = rounds.get(currentRound);
            currentRoundQuestions = round.questions;
            currentRoundAnswers = new ArrayList<>(
                Collections.nCopies(currentRoundQuestions.size(), null)
            );
            currentQuestionIndex = 0;
        }
    }
    
    /**
     * Shuffle the options of a question
     */
    private void shuffleQuestionOptions(Question question) {
        List<OptionPair> options = new ArrayList<>();
        options.add(new OptionPair('A', question.getOptionA()));
        options.add(new OptionPair('B', question.getOptionB()));
        options.add(new OptionPair('C', question.getOptionC()));
        options.add(new OptionPair('D', question.getOptionD()));
        
        String correctText = question.getCorrectAnswerText();
        Collections.shuffle(options);
        
        question.setOptionA(options.get(0).text);
        question.setOptionB(options.get(1).text);
        question.setOptionC(options.get(2).text);
        question.setOptionD(options.get(3).text);
        
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).text.equals(correctText)) {
                question.setCorrectOption((char)('A' + i));
                break;
            }
        }
    }
    
    /**
     * Helper class for option shuffling
     */
    private static class OptionPair {
        String text;
        OptionPair(char label, String text) {
            this.text = text;
        }
    }
    
    /**
     * Get the current question
     */
    public Question getCurrentQuestion() {
        if (currentQuestionIndex < currentRoundQuestions.size()) {
            return currentRoundQuestions.get(currentQuestionIndex);
        }
        return null;
    }
    
    /**
     * Submit an answer for the current question
     */
    public void submitAnswer(char answer) {
        if (currentQuestionIndex < currentRoundAnswers.size()) {
            currentRoundAnswers.set(currentQuestionIndex, answer);
        }
    }
    
    /**
     * Move to the next question
     * @return true if moved to next question, false if round is finished
     */
    public boolean nextQuestion() {
        currentQuestionIndex++;
        
        if (currentQuestionIndex >= currentRoundQuestions.size()) {
            return false; // Round finished
        }
        
        return true; // More questions in this round
    }
    
    /**
     * Move to previous question
     */
    public boolean previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            return true;
        }
        return false;
    }
    
    /**
     * Get current answer
     */
    public Character getCurrentAnswer() {
        if (currentQuestionIndex < currentRoundAnswers.size()) {
            return currentRoundAnswers.get(currentQuestionIndex);
        }
        return null;
    }
    
    /**
     * Calculate score for current round
     */
    private int calculateRoundScore() {
        int score = 0;
        for (int i = 0; i < currentRoundQuestions.size(); i++) {
            if (i < currentRoundAnswers.size() && currentRoundAnswers.get(i) != null) {
                if (currentRoundAnswers.get(i) == currentRoundQuestions.get(i).getCorrectOption()) {
                    score++;
                }
            }
        }
        return score;
    }
    
    /**
     * Finish the current round and get results
     * @return RoundResult object with score and details
     */
    public RoundResult finishRound() {
        int score = calculateRoundScore();
        int total = currentRoundQuestions.size();
        
        // Store round data
        RoundData roundData = rounds.get(currentRound);
        roundData.score = score;
        roundData.totalQuestions = total;
        roundData.completed = true;
        
        RoundResult result = new RoundResult(
            currentRound + 1,
            score,
            total,
            (score * 100.0) / total
        );
        
        return result;
    }
    
    /**
     * Move to the next round
     * @return true if moved to next round, false if all rounds completed
     */
    public boolean nextRound() {
        currentRound++;
        
        if (currentRound >= TOTAL_ROUNDS) {
            isGameActive = false;
            return false; // Game finished
        }
        
        startCurrentRound();
        return true; // More rounds available
    }
    
    /**
     * Finish the entire game and save to database
     * @return GameResult object with all round scores and overall stats
     */
    public GameResult finishGame() {
        if (DBLogin.currentUser == null) {
            return null;
        }
        
        // Calculate overall statistics
        int totalScore = 0;
        int totalQuestions = 0;
        List<Integer> roundScores = new ArrayList<>();
        
        for (RoundData round : rounds) {
            if (round.completed) {
                totalScore += round.score;
                totalQuestions += round.totalQuestions;
                roundScores.add(round.score);
            }
        }
        
        double averageScore = totalScore / (double) TOTAL_ROUNDS;
        double percentage = (totalScore * 100.0) / totalQuestions;
        
        // Save to database (ONE entry for the entire game)
        int userId = DBLogin.currentUser.getUserId();
        QuizAttempt attempt = new QuizAttempt(userId, difficultyLevel, totalScore, totalQuestions);
        boolean saved = DBQuizAttempt.saveAttempt(attempt);
        
        if (saved) {
            isGameActive = false;
            return new GameResult(
                roundScores,
                totalScore,
                totalQuestions,
                averageScore,
                percentage
            );
        }
        
        return null;
    }
    
    /**
     * Get total number of rounds
     */
    public int getTotalRounds() {
        return TOTAL_ROUNDS;
    }
    
    /**
     * Get current round number (1-based)
     */
    public int getCurrentRoundNumber() {
        return currentRound + 1;
    }
    
    /**
     * Get total questions in current round
     */
    public int getQuestionsPerRound() {
        return QUESTIONS_PER_ROUND;
    }
    
    /**
     * Get current question number in this round (1-based)
     */
    public int getCurrentQuestionNumber() {
        return currentQuestionIndex + 1;
    }
    
    /**
     * Get difficulty level
     */
    public String getDifficultyLevel() {
        return difficultyLevel;
    }
    
    /**
     * Check if game is active
     */
    public boolean isActive() {
        return isGameActive;
    }
    
    /**
     * Get all completed round results
     */
    public List<RoundResult> getAllRoundResults() {
        List<RoundResult> results = new ArrayList<>();
        
        for (int i = 0; i < rounds.size(); i++) {
            RoundData round = rounds.get(i);
            if (round.completed) {
                results.add(new RoundResult(
                    i + 1,
                    round.score,
                    round.totalQuestions,
                    (round.score * 100.0) / round.totalQuestions
                ));
            }
        }
        
        return results;
    }
    
    /**
     * Inner class to store data for one round
     */
    private static class RoundData {
        List<Question> questions;
        int score;
        int totalQuestions;
        boolean completed;
        
        RoundData(List<Question> questions) {
            this.questions = questions;
            this.score = 0;
            this.totalQuestions = questions.size();
            this.completed = false;
        }
    }
    
    /**
     * Class to represent result of one round
     */
    public static class RoundResult {
        public int roundNumber;
        public int score;
        public int totalQuestions;
        public double percentage;
        
        public RoundResult(int roundNumber, int score, int totalQuestions, double percentage) {
            this.roundNumber = roundNumber;
            this.score = score;
            this.totalQuestions = totalQuestions;
            this.percentage = percentage;
        }
        
        @Override
        public String toString() {
            return String.format("Round %d: %d/%d (%.1f%%)", 
                roundNumber, score, totalQuestions, percentage);
        }
    }
    
    /**
     * Class to represent final game result
     */
    public static class GameResult {
        public List<Integer> roundScores;      // Score for each round
        public int totalScore;                 // Sum of all rounds
        public int totalQuestions;             // Total questions across all rounds
        public double averageScore;            // Average score per round
        public double overallPercentage;       // Overall percentage
        
        public GameResult(List<Integer> roundScores, int totalScore, int totalQuestions, 
                         double averageScore, double overallPercentage) {
            this.roundScores = roundScores;
            this.totalScore = totalScore;
            this.totalQuestions = totalQuestions;
            this.averageScore = averageScore;
            this.overallPercentage = overallPercentage;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Game Results:\n");
            for (int i = 0; i < roundScores.size(); i++) {
                sb.append(String.format("Round %d: %d\n", i + 1, roundScores.get(i)));
            }
            sb.append(String.format("\nTotal Score: %d/%d\n", totalScore, totalQuestions));
            sb.append(String.format("Average: %.1f\n", averageScore));
            sb.append(String.format("Percentage: %.1f%%", overallPercentage));
            return sb.toString();
        }
    }
}