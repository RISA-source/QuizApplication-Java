package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.QuizAttempt;

/**
 * Database handler for QuizAttempt-related operations
 * Manages quiz scores, attempts, and leaderboard data
 */
public class DBQuizAttempt {

    /**
     * Save a quiz attempt to the database
     * @param attempt QuizAttempt object to save
     * @return true if successful, false otherwise
     */
    public static boolean saveAttempt(QuizAttempt attempt) {
        String sql = "INSERT INTO quiz_attempts (user_id, difficulty_level, score, total_questions) VALUES (?, ?, ?, ?)";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setInt(1, attempt.getUserId());
            pst.setString(2, attempt.getDifficultyLevel());
            pst.setInt(3, attempt.getScore());
            pst.setInt(4, attempt.getTotalQuestions());
            
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving quiz attempt: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all attempts by a specific user
     * @param userId User's ID
     * @return List of quiz attempts
     */
    public static List<QuizAttempt> getAttemptsByUser(int userId) {
        List<QuizAttempt> attempts = new ArrayList<>();
        String sql = "SELECT * FROM quiz_attempts WHERE user_id = ? ORDER BY attempted_at DESC";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                QuizAttempt attempt = new QuizAttempt(
                    rs.getInt("attempt_id"),
                    rs.getInt("user_id"),
                    rs.getString("difficulty_level"),
                    rs.getInt("score"),
                    rs.getInt("total_questions"),
                    rs.getTimestamp("attempted_at")
                );
                attempts.add(attempt);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user attempts: " + e.getMessage());
        }
        return attempts;
    }

    /**
     * Get leaderboard for a specific difficulty level
     * @param difficultyLevel "Beginner", "Intermediate", or "Advanced"
     * @param limit Maximum number of results
     * @return List of top quiz attempts with usernames
     */
    public static List<QuizAttempt> getLeaderboard(String difficultyLevel, int limit) {
        List<QuizAttempt> leaderboard = new ArrayList<>();
        String sql = "SELECT qa.attempt_id, qa.user_id, u.username, qa.difficulty_level, qa.score, qa.total_questions, qa.attempted_at " +
                     "FROM quiz_attempts qa " +
                     "JOIN users u ON qa.user_id = u.user_id " +
                     "WHERE qa.difficulty_level = ? " +
                     "ORDER BY qa.score DESC, qa.attempted_at ASC " +
                     "LIMIT ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setString(1, difficultyLevel);
            pst.setInt(2, limit);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                QuizAttempt attempt = new QuizAttempt(
                    rs.getInt("attempt_id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("difficulty_level"),
                    rs.getInt("score"),
                    rs.getInt("total_questions"),
                    rs.getTimestamp("attempted_at")
                );
                leaderboard.add(attempt);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching leaderboard: " + e.getMessage());
        }
        return leaderboard;
    }

    /**
     * Get user's best score for a specific difficulty
     * @param userId User's ID
     * @param difficultyLevel Difficulty level
     * @return Best score or 0 if no attempts
     */
    public static int getUserBestScore(int userId, String difficultyLevel) {
        String sql = "SELECT MAX(score) as best_score FROM quiz_attempts WHERE user_id = ? AND difficulty_level = ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setInt(1, userId);
            pst.setString(2, difficultyLevel);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("best_score");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching best score: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get total number of attempts by a user
     * @param userId User's ID
     * @return Total attempts count
     */
    public static int getUserTotalAttempts(int userId) {
        String sql = "SELECT COUNT(*) as total FROM quiz_attempts WHERE user_id = ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error counting attempts: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get user's average score across all difficulties
     * @param userId User's ID
     * @return Average score
     */
    public static double getUserAverageScore(int userId) {
        String sql = "SELECT AVG(score) as avg_score FROM quiz_attempts WHERE user_id = ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("avg_score");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating average score: " + e.getMessage());
        }
        return 0.0;
    }

    /**
     * Get highest score across all users for a difficulty level
     * @param difficultyLevel Difficulty level
     * @return Highest score
     */
    public static int getHighestScoreForDifficulty(String difficultyLevel) {
        String sql = "SELECT MAX(score) as highest FROM quiz_attempts WHERE difficulty_level = ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setString(1, difficultyLevel);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("highest");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching highest score: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get total number of quiz attempts across all users
     * @return Total attempts
     */
    public static int getTotalAttempts() {
        String sql = "SELECT COUNT(*) as total FROM quiz_attempts";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error counting total attempts: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get recent attempts across all users (for admin view)
     * @param limit Maximum number of results
     * @return List of recent attempts with usernames
     */
    public static List<QuizAttempt> getRecentAttempts(int limit) {
        List<QuizAttempt> attempts = new ArrayList<>();
        String sql = "SELECT qa.attempt_id, qa.user_id, u.username, qa.difficulty_level, qa.score, qa.total_questions, qa.attempted_at " +
                     "FROM quiz_attempts qa " +
                     "JOIN users u ON qa.user_id = u.user_id " +
                     "ORDER BY qa.attempted_at DESC " +
                     "LIMIT ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setInt(1, limit);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                QuizAttempt attempt = new QuizAttempt(
                    rs.getInt("attempt_id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("difficulty_level"),
                    rs.getInt("score"),
                    rs.getInt("total_questions"),
                    rs.getTimestamp("attempted_at")
                );
                attempts.add(attempt);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching recent attempts: " + e.getMessage());
        }
        return attempts;
    }
}