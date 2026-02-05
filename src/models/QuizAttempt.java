package models;

import java.sql.Timestamp;

/**
 * Represents a quiz attempt/score record
 * Used to store and transfer quiz attempt data between database and UI
 */
public class QuizAttempt {
    private int attemptId;
    private int userId;
    private String username; // For display purposes (not stored in DB, fetched via JOIN)
    private String difficultyLevel; // "Beginner", "Intermediate", "Advanced"
    private int score;
    private int totalQuestions;
    private Timestamp attemptedAt;

    /**
     * Default constructor
     */
    public QuizAttempt() {
    }

    /**
     * Constructor without ID and timestamp (for inserting new attempts)
     */
    public QuizAttempt(int userId, String difficultyLevel, int score, int totalQuestions) {
        this.userId = userId;
        this.difficultyLevel = difficultyLevel;
        this.score = score;
        this.totalQuestions = totalQuestions;
    }

    /**
     * Constructor with all fields (for fetching from database)
     */
    public QuizAttempt(int attemptId, int userId, String difficultyLevel, 
                      int score, int totalQuestions, Timestamp attemptedAt) {
        this.attemptId = attemptId;
        this.userId = userId;
        this.difficultyLevel = difficultyLevel;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.attemptedAt = attemptedAt;
    }

    /**
     * Constructor with username (for display in leaderboards)
     */
    public QuizAttempt(int attemptId, int userId, String username, String difficultyLevel, 
                      int score, int totalQuestions, Timestamp attemptedAt) {
        this.attemptId = attemptId;
        this.userId = userId;
        this.username = username;
        this.difficultyLevel = difficultyLevel;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.attemptedAt = attemptedAt;
    }

    // Getters and Setters
    public int getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(int attemptId) {
        this.attemptId = attemptId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Timestamp getAttemptedAt() {
        return attemptedAt;
    }

    public void setAttemptedAt(Timestamp attemptedAt) {
        this.attemptedAt = attemptedAt;
    }

    /**
     * Calculate percentage score
     */
    public double getPercentage() {
        if (totalQuestions == 0) return 0.0;
        return (score * 100.0) / totalQuestions;
    }

    @Override
    public String toString() {
        return "QuizAttempt{" +
                "attemptId=" + attemptId +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", score=" + score +
                ", totalQuestions=" + totalQuestions +
                ", attemptedAt=" + attemptedAt +
                '}';
    }
}