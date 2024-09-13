package main.java.dao;

import main.java.config.Session;
import main.java.dao.interfaces.DocumentDAOInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DocumentDAO implements DocumentDAOInterface {

    private final Connection connection;
    private Session session = Session.getInstance();

    public DocumentDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void lendDocument(String id, String userId) throws SQLException {
        String lendQuery = "UPDATE documents SET borrowedBy = ?::uuid WHERE id = ?::uuid";
        try (PreparedStatement lendStmt = connection.prepareStatement(lendQuery)) {
            lendStmt.setString(1, userId);
            lendStmt.setString(2, id);
            lendStmt.executeUpdate();
        }
    }

    @Override
    public void returnDocument(String id) throws SQLException {
        String returnQuery = "UPDATE documents SET borrowedBy = NULL WHERE id = ?::uuid";
        try (PreparedStatement returnStmt = connection.prepareStatement(returnQuery)) {
            returnStmt.setString(1, id);
            returnStmt.executeUpdate();
        }
    }

    @Override
    public void reserveDocument(String id, String userId) throws SQLException {
        String reserveQuery = "UPDATE documents SET reservedBy = ?::uuid WHERE id = ?::uuid";
        try (PreparedStatement reserveStmt = connection.prepareStatement(reserveQuery)) {
            reserveStmt.setString(1, userId);
            reserveStmt.setString(2, id);
            reserveStmt.executeUpdate();
        }
    }

    @Override
    public void cancelReservation(String id) throws SQLException {
        String cancelQuery = "UPDATE documents SET reservedBy = NULL WHERE id = ?::uuid";
        try (PreparedStatement cancelStmt = connection.prepareStatement(cancelQuery)) {
            cancelStmt.setString(1, id);
            cancelStmt.executeUpdate();
        }
    }

    public ResultSet checkDocumentStatus(String id) throws SQLException {
        String checkQuery = "SELECT borrowedBy, reservedBy FROM documents WHERE id = ?::uuid";
        PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
        checkStmt.setString(1, id);
        return checkStmt.executeQuery();
    }

    public void updateBorrowLimit(String userId, int increment) throws SQLException {
        String updateLimitQuery = "UPDATE students SET borrowLimit = borrowLimit + ? WHERE id = ?::uuid";
        try (PreparedStatement updateLimitStmt = connection.prepareStatement(updateLimitQuery)) {
            updateLimitStmt.setInt(1, increment);
            updateLimitStmt.setString(2, userId);
            updateLimitStmt.executeUpdate();
        }
    }

    public ResultSet getBorrowLimit(String userId) throws SQLException {
        String checkLimitQuery = "SELECT borrowLimit FROM students WHERE id = ?::uuid";
        PreparedStatement checkLimitStmt = connection.prepareStatement(checkLimitQuery);
        checkLimitStmt.setString(1, userId);
        return checkLimitStmt.executeQuery();
    }

    public ResultSet getUserDocuments() throws SQLException {
        String  userId = session.getId().toString();
        String userDocumentsQuery = "SELECT *, tableoid::regclass::text as src_table FROM documents WHERE borrowedBy = ?::uuid OR reservedBy = ?::uuid";
        PreparedStatement userDocumentsStmt = connection.prepareStatement(userDocumentsQuery);
        userDocumentsStmt.setString(1, userId);
        userDocumentsStmt.setString(2, userId);

        return userDocumentsStmt.executeQuery();
    }
}
