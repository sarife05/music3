package org.example.musiclibrary.utils;

import org.example.musiclibrary.exception.DatabaseOperationException;
import java.sql.*;

/**
 * DatabaseConnection manages the SQLite database connection.
 * Follows SRP: Single responsibility is managing database connections.
 * Uses DriverManager (as per requirements).
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/musiclibrary";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";
    private static Connection connection = null;

    /**
     * Get database connection (singleton pattern)
     */
    public static Connection getConnection() throws DatabaseOperationException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✓ Database connection established");
            }
            return connection;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to connect to database", e);
        }
    }

    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    /**
     * Close PreparedStatement safely
     */
    public static void closePreparedStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
    }

    /**
     * Close ResultSet safely
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
        }
    }

    /**
     * Execute a simple query and return result count (for testing)
     */
    public static int executeCountQuery(String query) throws DatabaseOperationException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to execute count query", e);
        }
    }
}