package main.java;

import main.java.config.ConnectionManager;
import main.java.ui.ConsoleUI;

import java.sql.Connection;

public class LibraryApp {
    public static void main(String[] args) {
        Connection connection = ConnectionManager.getInstance().getConnection();
        ConsoleUI consoleUI = new ConsoleUI(connection);
        ConnectionManager.getInstance().closeConnection();
    }
}
