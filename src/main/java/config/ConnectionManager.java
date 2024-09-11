package main.java.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {

    private static final String PROPERTIES_FILE = "app.properties";

    private ConnectionManager() {
    }

    public static Connection getConnection() throws SQLException {
        try (InputStream input = ConnectionManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + PROPERTIES_FILE + " file.");
            }
            Properties properties = new Properties();
            properties.load(input);

            String driver = properties.getProperty("driver");
            String url = properties.getProperty("url");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");

            Class.forName(driver);

            return DriverManager.getConnection(url, username, password);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error establishing database connection: " + e.getMessage(), e);
        }
    }
}