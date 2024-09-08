package main.java.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5434/ShelfMaster";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "zineb2003";


    private static ConnectionManager instance;


    private Connection connection;


    private ConnectionManager() {
        try {

            this.connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static ConnectionManager getInstance() {
        if (instance == null) {
            synchronized (ConnectionManager.class) {
                if (instance == null) {
                    instance = new ConnectionManager();
                }
            }
        }
        return instance;
    }


    public Connection getConnection() {
        return connection;
    }


    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing the connection: " + e.getMessage());
            }
        }
    }
}
