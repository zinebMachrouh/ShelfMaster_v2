package main.java.business.Interfaces;

import java.sql.SQLException;
import java.util.Scanner;

public interface LendableInterface {
    void lendDocument(Scanner scanner, String documentId) throws SQLException;
    void returnDocument(String id) throws SQLException;
}
