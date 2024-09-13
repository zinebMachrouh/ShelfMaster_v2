package main.java.dao.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DocumentDAOInterface {
    void lendDocument(String id, String userId) throws SQLException;

    void returnDocument(String id) throws SQLException;

    void reserveDocument(String id, String userId) throws SQLException;

    void cancelReservation(String id) throws SQLException;

    ResultSet checkDocumentStatus(String id) throws SQLException;
}
