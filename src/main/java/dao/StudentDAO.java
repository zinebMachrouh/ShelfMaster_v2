package main.java.dao;

import main.java.dao.interfaces.StudentDAOInterface;
import main.java.utils.InputValidator;

import java.sql.*;
import java.util.UUID;

public class StudentDAO implements StudentDAOInterface {
    private final Connection connection;
    InputValidator inputValidator = new InputValidator();
    public StudentDAO(Connection connection) {
        this.connection = connection;

    }

    @Override
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

    @Override
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

    @Override
    public void deleteStudent(String id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM students WHERE id = ?");
        ps.setString(1, id);
        ps.executeUpdate();
        ps.close();
        System.out.println("Student deleted successfully");
    }

    @Override
    public boolean studentExists(String email) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM students WHERE email = ?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        boolean exists = rs.next();
        rs.close();
        ps.close();

        return exists;
    }

    @Override
    public ResultSet getStudent(String searchTerm) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            if (inputValidator.validateId(searchTerm)) {
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


    @Override
    public ResultSet getAllStudents() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM students");
        return ps.executeQuery();
    }


}