package main.java.dao;

import main.java.dao.interfaces.StudentDAOInterface;

import java.sql.*;

public class StudentDAO implements StudentDAOInterface {
    Connection connection;

    public StudentDAO(Connection connection) {
        this.connection = connection;
    }

    public void addStudent(String name, String email, String studyProgram) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO students (name, email, studyprogram) VALUES (?, ?, ?)");
        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, studyProgram);
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

    public boolean studentExists(String id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM students WHERE id = ?");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        boolean exists = rs.next();
        rs.close();
        ps.close();

        return exists;
    }

    public void getStudent(String searchTerm) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM students where id = ? OR  email = ?");
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            ResultSet rs = ps.executeQuery();
            displayData(rs);
            rs.close();
            ps.close();

        } catch (SQLClientInfoException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
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