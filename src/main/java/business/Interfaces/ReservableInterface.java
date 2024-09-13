package main.java.business.Interfaces;

import java.sql.SQLException;
import java.util.Scanner;

public interface ReservableInterface {
    void reserveDocument(Scanner scanner, String documentId) throws SQLException;
    void cancelReservation(String id) throws SQLException;
}
