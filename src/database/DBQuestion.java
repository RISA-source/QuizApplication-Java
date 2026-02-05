package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Question;

/**
 * Database handler for Question-related operations
 * Provides CRUD operations for managing quiz questions
 */
public class DBQuestion {

    /**
     * Add a new question to the database
     * @param question Question object to be added
     * @return true if successful, false otherwise
     */
    public static boolean addQuestion(Question question) {
        String sql = "INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_option, difficulty_level) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setString(1, question.getQuestionText());
            pst.setString(2, question.getOptionA());
            pst.setString(3, question.getOptionB());
            pst.setString(4, question.getOptionC());
            pst.setString(5, question.getOptionD());
            pst.setString(6, String.valueOf(question.getCorrectOption()));
            pst.setString(7, question.getDifficultyLevel());
            
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error adding question: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get a question by its ID
     * @param questionId ID of the question
     * @return Question object or null if not found
     */
    public static Question getQuestionById(int questionId) {
        String sql = "SELECT * FROM questions WHERE question_id = ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setInt(1, questionId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return new Question(
                    rs.getInt("question_id"),
                    rs.getString("question_text"),
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d"),
                    rs.getString("correct_option").charAt(0),
                    rs.getString("difficulty_level")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching question: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all questions from the database
     * @return List of all questions
     */
    public static List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM questions ORDER BY question_id";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()
        ) {
            while (rs.next()) {
                Question q = new Question(
                    rs.getInt("question_id"),
                    rs.getString("question_text"),
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d"),
                    rs.getString("correct_option").charAt(0),
                    rs.getString("difficulty_level")
                );
                questions.add(q);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all questions: " + e.getMessage());
        }
        return questions;
    }

    /**
     * Get questions by difficulty level
     * @param difficultyLevel "Beginner", "Intermediate", or "Advanced"
     * @return List of questions for that difficulty
     */
    public static List<Question> getQuestionsByDifficulty(String difficultyLevel) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE difficulty_level = ? ORDER BY RAND()";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setString(1, difficultyLevel);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Question q = new Question(
                    rs.getInt("question_id"),
                    rs.getString("question_text"),
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d"),
                    rs.getString("correct_option").charAt(0),
                    rs.getString("difficulty_level")
                );
                questions.add(q);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching questions by difficulty: " + e.getMessage());
        }
        return questions;
    }

    /**
     * Update an existing question
     * @param question Question object with updated data
     * @return true if successful, false otherwise
     */
    public static boolean updateQuestion(Question question) {
        String sql = "UPDATE questions SET question_text = ?, option_a = ?, option_b = ?, option_c = ?, option_d = ?, correct_option = ?, difficulty_level = ? WHERE question_id = ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setString(1, question.getQuestionText());
            pst.setString(2, question.getOptionA());
            pst.setString(3, question.getOptionB());
            pst.setString(4, question.getOptionC());
            pst.setString(5, question.getOptionD());
            pst.setString(6, String.valueOf(question.getCorrectOption()));
            pst.setString(7, question.getDifficultyLevel());
            pst.setInt(8, question.getQuestionId());
            
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating question: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a question by ID
     * @param questionId ID of the question to delete
     * @return true if successful, false otherwise
     */
    public static boolean deleteQuestion(int questionId) {
        String sql = "DELETE FROM questions WHERE question_id = ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setInt(1, questionId);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting question: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get total count of questions
     * @return Total number of questions in database
     */
    public static int getTotalQuestionCount() {
        String sql = "SELECT COUNT(*) as total FROM questions";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error counting questions: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get count of questions by difficulty level
     * @param difficultyLevel "Beginner", "Intermediate", or "Advanced"
     * @return Number of questions for that difficulty
     */
    public static int getQuestionCountByDifficulty(String difficultyLevel) {
        String sql = "SELECT COUNT(*) as total FROM questions WHERE difficulty_level = ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setString(1, difficultyLevel);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error counting questions by difficulty: " + e.getMessage());
        }
        return 0;
    }
}