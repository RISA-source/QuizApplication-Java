package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.User;

/**
 * Database handler for User-related operations
 * Provides methods for user management, search, and statistics
 */
public class DBUser {

    /**
     * Get user by ID
     * @param userId User's ID
     * @return User object or null if not found
     */
    public static User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get user by email
     * @param email User's email
     * @return User object or null if not found
     */
    public static User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user by email: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get user by username
     * @param username User's username
     * @return User object or null if not found
     */
    public static User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user by username: " + e.getMessage());
        }
        return null;
    }

    /**
     * Search users by username or email (partial match)
     * @param searchTerm Search string
     * @return List of matching users
     */
    public static List<User> searchUsers(String searchTerm) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE username LIKE ? OR email LIKE ? ORDER BY username";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            String searchPattern = "%" + searchTerm + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getTimestamp("created_at")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Error searching users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Get all users from database
     * @return List of all users
     */
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()
        ) {
            while (rs.next()) {
                User user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getTimestamp("created_at")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Get total count of registered users
     * @return Total user count
     */
    public static int getTotalUserCount() {
        String sql = "SELECT COUNT(*) as total FROM users";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error counting users: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Update user information
     * @param user User object with updated data
     * @return true if successful, false otherwise
     */
    public static boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, password = ? WHERE user_id = ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setString(1, user.getUsername());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getPassword());
            pst.setInt(4, user.getUserId());
            
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a user by ID
     * Note: This will cascade delete all quiz attempts due to foreign key
     * @param userId User's ID
     * @return true if successful, false otherwise
     */
    public static boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setInt(1, userId);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if username already exists
     * @param username Username to check
     * @return true if exists, false otherwise
     */
    public static boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) as count FROM users WHERE username = ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking username: " + e.getMessage());
        }
        return false;
    }

    /**
     * Check if email already exists
     * @param email Email to check
     * @return true if exists, false otherwise
     */
    public static boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) as count FROM users WHERE email = ?";
        
        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking email: " + e.getMessage());
        }
        return false;
    }
}