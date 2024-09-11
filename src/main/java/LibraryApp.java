package main.java;

import main.java.config.ConnectionManager;
import main.java.ui.ConsoleUI;

import java.sql.Connection;
import java.sql.SQLException;

public class LibraryApp {
    public static void main(String[] args) throws SQLException {
        Connection connection = ConnectionManager.getConnection();
        ConsoleUI consoleUI = new ConsoleUI(connection);
        //ConnectionManager.getInstance().closeConnection();
    }
}
