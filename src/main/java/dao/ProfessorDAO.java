package main.java.dao;

import main.java.dao.interfaces.ProfessorDAOInterface;
import main.java.utils.InputValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ProfessorDAO implements ProfessorDAOInterface {
    private final Connection connection;
    InputValidator inputValidator = new InputValidator();

    public ProfessorDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addProfessor(String id, String name, String email, String department) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO professors (id, name, email, department) VALUES (?::uuid, ?, ?, ?)");
        ps.setString(1, id);
        ps.setString(2, name);
        ps.setString(3, email);
        ps.setString(4, department);
        ps.executeUpdate();
        ps.close();
        System.out.println("Professor added successfully.");
    }

    @Override
    public void updateProfessor(String id, String name, String email, String department) throws SQLException {
        if (name.isEmpty() && email.isEmpty() && department.isEmpty()) {
            System.out.println("No fields to update.");
            return;
        }

        StringBuilder sql = new StringBuilder("UPDATE professors SET ");
        boolean firstField = true;

        if (!name.isEmpty()) {
            sql.append("name = ?");
            firstField = false;
        }
        if (!email.isEmpty()) {
            if (!firstField) {
                sql.append(", ");
            }
            sql.append("email = ?");
            firstField = false;
        }
        if (!department.isEmpty()) {
            if (!firstField) {
                sql.append(", ");
            }
            sql.append("department = ?");
        }

        sql.append(" WHERE id = ?::uuid");

        PreparedStatement ps = connection.prepareStatement(sql.toString());

        int parameterIndex = 1;
        if (!name.isEmpty()) {
            ps.setString(parameterIndex++, name);
        }
        if (!email.isEmpty()) {
            ps.setString(parameterIndex++, email);
        }
        if (!department.isEmpty()) {
            ps.setString(parameterIndex++, department);
        }

        ps.setString(parameterIndex, id);

        ps.executeUpdate();
        ps.close();
        System.out.println("Professor updated successfully.");
    }

    @Override
    public void deleteProfessor(String id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM professors WHERE id = ?::uuid");
        ps.setString(1, id);
        ps.executeUpdate();
        ps.close();
        System.out.println("Professor deleted successfully.");
    }

    @Override
    public ResultSet getProfessor(String searchTerm) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            if (inputValidator.validateId(searchTerm)) {
                ps = connection.prepareStatement("SELECT * FROM professors WHERE id = ?::uuid");
                ps.setObject(1, UUID.fromString(searchTerm), java.sql.Types.OTHER);
            } else {
                ps = connection.prepareStatement("SELECT * FROM professors WHERE email = ?");
                ps.setString(1, searchTerm);
            }

            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }

        return rs;
    }

    @Override
    public ResultSet getAllProfessors() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM professors");
        return ps.executeQuery();
    }

    @Override
    public boolean professorExists(String searchTerm) throws SQLException {
        boolean exists = false;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            if (inputValidator.validateId(searchTerm)) {
                ps = connection.prepareStatement("SELECT * FROM professors WHERE id = ?::uuid");
                ps.setObject(1, UUID.fromString(searchTerm), java.sql.Types.OTHER);
            } else {
                ps = connection.prepareStatement("SELECT * FROM professors WHERE email = ?");
                ps.setString(1, searchTerm);
            }

            rs = ps.executeQuery();
            exists = rs.next();
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }

        return exists;
    }
}
