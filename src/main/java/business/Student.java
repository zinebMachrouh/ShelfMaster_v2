package main.java.business;

import main.java.dao.StudentDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

public class Student {
    private UUID id;
    private String name;
    private String email;
    private String studyProgram;

    public StudentDAO studentDAO;

    public Student(String name, String email, String studyProgram,Connection connection){
        this.id = generateUuid();
        this.name = name;
        this.email = email;
        this.studyProgram = studyProgram;

        this.studentDAO = new StudentDAO(connection);
    }

    public Student(Connection connection){
        this.id = null;
        this.name = "";
        this.email = "";
        this.studyProgram = "";

        this.studentDAO = new StudentDAO(connection);
    }

    private UUID generateUuid() {
        return UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getStudyProgram() {
        return studyProgram;
    }

    public UUID getId() {
        return id;
    }

    public void addStudent(Scanner scanner) throws SQLException {
        System.out.println("Enter student name: ");
        this.name = scanner.nextLine();
        System.out.println("Enter student email: ");
        this.email = scanner.nextLine();
        System.out.println("Enter student study program: ");
        this.studyProgram = scanner.nextLine();

        this.id = generateUuid();

        studentDAO.addStudent(this.id.toString(), this.name, this.email, this.studyProgram);
    }

    public boolean studentExists(String email) throws SQLException {
        boolean exists = studentDAO.studentExists(email);
        if (exists){
            ResultSet rs = studentDAO.getStudent(email);
            while (rs.next()){
                this.id = UUID.fromString(rs.getString("id"));
                this.name = rs.getString("name");
                this.email = rs.getString("email");
                this.studyProgram = rs.getString("studyprogram");
            }
        } else {
            System.out.println("Student does not exist.");
        }
        return exists;
    }

    public void updateStudent(Scanner scanner) throws SQLException {
        System.out.println("Enter student name: ");
        if (scanner.hasNextLine()){
            this.name = scanner.nextLine();
        }else {
            this.name = "";
        }
        System.out.println("Enter student email: ");
        if (scanner.hasNextLine()){
            this.email = scanner.nextLine();
        }else {
            this.email = "";
        }
        System.out.println("Enter student study program: ");
        if (scanner.hasNextLine()){
            this.studyProgram = scanner.nextLine();
        }else {
            this.studyProgram = "";
        }

        studentDAO.updateStudent(this.id.toString(), this.name, this.email, this.studyProgram);
    }

    public void deleteStudent() throws SQLException {
        studentDAO.deleteStudent(this.id.toString());
    }

    public void getStudents() throws SQLException {
        displayData(studentDAO.getAllStudents());
    }

    public void getStudent(String email) throws SQLException {
        ResultSet rs = studentDAO.getStudent(email);
        while (rs.next()){
            this.id = UUID.fromString(rs.getString("id"));
            this.name = rs.getString("name");
            this.email = rs.getString("email");
            this.studyProgram = rs.getString("studyprogram");
        }
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

    public void studentMenu(Scanner scanner) {
    }
}
