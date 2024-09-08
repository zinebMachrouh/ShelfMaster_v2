package main.java.dao;

import main.java.dao.interfaces.StudentDAOInterface;

import java.sql.*;
import java.util.UUID;

public class StudentDAO implements StudentDAOInterface {
    private final Connection connection;

    public StudentDAO(Connection connection) {
        this.connection = connection;
    }

    public void addStudent(String id,String name, String email, String studyProgram) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO students (id,name, email, studyprogram) VALUES (?::uuid,?, ?, ?)");
        ps.setString(1, id);
        ps.setString(2, name);
        ps.setString(3, email);
        ps.setString(4, studyProgram);
        ps.executeUpdate();
        ps.close();
        System.out.println("Student added successfully");
    }

    public void updateStudent(String id, String name, String email, String studyProgram) throws SQLException {
        if (name.isEmpty() && email.isEmpty() && studyProgram.isEmpty()) {
            System.out.println("No fields to update.");
            return;
        }

        StringBuilder sql = new StringBuilder("UPDATE students SET ");
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
        if (!studyProgram.isEmpty()) {
            if (!firstField) {
                sql.append(", ");
            }
            sql.append("studyprogram = ?");
        }

        sql.append(" WHERE id = ?");


        PreparedStatement ps = connection.prepareStatement(sql.toString());

        int parameterIndex = 1;
        if (!name.isEmpty()) {
            ps.setString(parameterIndex++, name);
        }
        if (!email.isEmpty()) {
            ps.setString(parameterIndex++, email);
        }
        if (!studyProgram.isEmpty()) {
            ps.setString(parameterIndex++, studyProgram);
        }

        ps.setString(parameterIndex, id);


        ps.executeUpdate();
        ps.close();

        System.out.println("Student updated successfully.");
    }

    public void deleteStudent(String id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM students WHERE id = ?");
        ps.setString(1, id);
        ps.executeUpdate();
        ps.close();
        System.out.println("Student deleted successfully");
    }

    public boolean studentExists(String email) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM students WHERE email = ?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        boolean exists = rs.next();
        rs.close();
        ps.close();

        return exists;
    }

    public ResultSet getStudent(String searchTerm) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            if (isValidUUID(searchTerm)) {
                ps = connection.prepareStatement("SELECT * FROM students WHERE id = ?::uuid");
                ps.setObject(1, searchTerm, java.sql.Types.OTHER);
            } else {
                ps = connection.prepareStatement("SELECT * FROM students WHERE email = ?");
                ps.setString(1, searchTerm);
            }

            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }

        return rs;
    }

    private boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public void getAllStudents() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM students");
        ResultSet rs = ps.executeQuery();
        displayData(rs);
        rs.close();
        ps.close();
        System.out.println("All students retrieved");
    }

    public void displayData(ResultSet rs) {
        try {
            while (rs.next()) {
                System.out.println("Student ID: " + rs.getInt("id"));
                System.out.println("Student Name: " + rs.getString("name"));
                System.out.println("Student Email: " + rs.getString("email"));
                System.out.println("Student Study Program: " + rs.getString("studyprogram"));
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}