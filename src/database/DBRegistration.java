package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBRegistration {

    public static boolean registerUser(String username, String email, String password) {

        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        try (
            Connection conn = DBConnection.connection();
            PreparedStatement pst = conn.prepareStatement(sql)
        ) {

            pst.setString(1, username);
            pst.setString(2, email);
            pst.setString(3, password);

            pst.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Registration Error: " + e.getMessage());
            return false;
        }
    }
}
