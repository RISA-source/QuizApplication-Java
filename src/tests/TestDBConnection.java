package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import database.DBConnection;

/**
 * Test class for database connection
 */
@DisplayName("Database Connection Tests")
class TestDBConnection {

    private static Connection connection;

    @BeforeAll
    @DisplayName("Setup - Establish database connection")
    static void setupBeforeAll() {
        System.out.println("Setting up database connection tests...");
        connection = DBConnection.connection();
    }

    @Test
    @DisplayName("Test: Database connection is not null")
    void testConnectionNotNull() {
        assertNotNull(connection, "Database connection should not be null");
    }

    @Test
    @DisplayName("Test: Database connection is valid")
    void testConnectionIsValid() throws Exception {
        assertTrue(connection.isValid(5), "Database connection should be valid within 5 seconds");
    }

    @Test
    @DisplayName("Test: Database connection is not closed")
    void testConnectionIsNotClosed() throws Exception {
        assertFalse(connection.isClosed(), "Database connection should not be closed");
    }

    @Test
    @DisplayName("Test: Can create new connection")
    void testCanCreateNewConnection() {
        Connection newConnection = DBConnection.connection();
        assertNotNull(newConnection, "Should be able to create a new database connection");
    }

    @AfterAll
    @DisplayName("Cleanup - Close database connection")
    static void cleanupAfterAll() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Database connection closed successfully.");
        }
    }
}