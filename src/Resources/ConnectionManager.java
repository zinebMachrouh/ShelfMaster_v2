package Resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://localhost:5434/ShelfMaster";
        String username = "postgres";
        String password = "zineb2003";

        Connection connection = null;

        try {
            // Connect to the database
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connected to the database successfully.");

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure the connection is closed to prevent resource leaks
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Connection closed.");
                } catch (SQLException e) {
                    System.err.println("Error closing the connection: " + e.getMessage());
                }
            }
        }
    }
}
