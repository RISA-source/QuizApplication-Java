package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection connection() {

        Connection conn = null;

        String url = "jdbc:mysql://localhost:3306/quiz_db";
        String username = "root";
        String password = "";

        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Quiz DB Connected!");

        } catch (SQLException e) {
            System.out.println("DB Error: " + e.getMessage());
        }

        return conn;
    }
}
