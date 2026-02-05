package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.User;

/**
 * Database handler for user login operations with session management
 * Stores the currently logged-in user in a static variable for easy access throughout the app
 */
public class DBLogin {
    
    // Static variable to hold the currently logged-in user
    // Access this directly from anywhere: DBLogin.currentUser
    public static User currentUser = null;
    
    /**
     * Login user and store their data in session
     * @param email User's email
     * @param password User's password
     * @return true if login successful, false otherwise
     */
    public static boolean loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                // Login successful - create and store User object
                currentUser = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getTimestamp("created_at")
                );
                return true;
            } else {
                // Login failed - clear any existing user
                currentUser = null;
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
            currentUser = null;
            return false;
        }
    }
    
    /**
     * Logout the current user (clear session)
     */
    public static void logout() {
        currentUser = null;
    }
}
