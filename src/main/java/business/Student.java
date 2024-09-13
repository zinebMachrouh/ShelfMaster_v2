package main.java.business;

import main.java.dao.StudentDAO;
import main.java.utils.InputValidator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Student extends User {
    private String studyProgram;
    public StudentDAO studentDAO;

    public Student(String name, String email, String studyProgram, Connection connection) {
        super(connection);
        this.setId(generateUuid());
        this.setName(name);
        this.setEmail(email);
        this.studyProgram = studyProgram;
        this.studentDAO = new StudentDAO(connection);
    }

    public Student(Connection connection) {
        super(connection);
        this.studyProgram = "";
        this.studentDAO = new StudentDAO(connection);
    }

    private UUID generateUuid() {
        return UUID.randomUUID();
    }

    public String getStudyProgram() {
        return studyProgram;
    }

    public void setStudyProgram(String studyProgram) {
        this.studyProgram = studyProgram;
    }

    public void addStudent(Scanner scanner) throws SQLException {
        String name;
        do {
            System.out.println("Enter student name: ");
            name = scanner.nextLine();
            if (!InputValidator.handleAuthor(name)) {
                System.out.println("Invalid input. Please enter a valid name.");
            }
        } while (!InputValidator.handleAuthor(name));
        setName(name);

        String email;
        do {
            System.out.println("Enter student email: ");
            email = scanner.nextLine();
            if (!InputValidator.validateEmail(email)) {
                System.out.println("Invalid input. Please enter a valid email.");
            }
        } while (!InputValidator.validateEmail(email));
        setEmail(email);

        String studyProgram;
        do {
            System.out.println("Enter student study program: ");
            studyProgram = scanner.nextLine();
            if (!InputValidator.handleAuthor(studyProgram)) {
                System.out.println("Invalid input. Please enter a valid study program.");
            }
        } while (!InputValidator.handleAuthor(studyProgram));
        this.studyProgram = studyProgram;

        setId(generateUuid());
        studentDAO.addStudent(getId().toString(), getName(), getEmail(), this.studyProgram);
    }


    public boolean studentExists(String email) throws SQLException {
        boolean exists = studentDAO.studentExists(email);
        if (exists) {
            ResultSet rs = studentDAO.getStudent(email);
            while (rs.next()) {
                setId(UUID.fromString(rs.getString("id")));
                setName(rs.getString("name"));
                setEmail(rs.getString("email"));
                this.studyProgram = rs.getString("studyprogram");
            }
        } else {
            System.out.println("Student does not exist.");
        }
        return exists;
    }

    public void updateStudent(Scanner scanner, String id) throws SQLException {
        System.out.println("Enter student name: ");
        if (scanner.hasNextLine()) {
            setName(scanner.nextLine());
        } else {
            setName(getName());
        }

        System.out.println("Enter student email: ");
        if (scanner.hasNextLine()) {
            setEmail(scanner.nextLine());
        } else {
            setEmail(getEmail());
        }

        System.out.println("Enter student study program: ");
        if (scanner.hasNextLine()) {
            this.studyProgram = scanner.nextLine();
        } else {
            this.studyProgram = this.studyProgram;
        }

        studentDAO.updateStudent(id, getName(), getEmail(), this.studyProgram);
    }


    public void deleteStudent() throws SQLException {
        studentDAO.deleteStudent(getId().toString());
    }

    public void getStudents() throws SQLException {
        displayData(studentDAO.getAllStudents());
    }

    public void getStudent(String searchTerm) throws SQLException {
        ResultSet rs = studentDAO.getStudent(searchTerm);
        displayData(rs);
    }

    public void displayData(ResultSet rs) {
        try {
            while (rs.next()) {
                System.out.println("Student ID: " + rs.getString("id"));
                System.out.println("Student Name: " + rs.getString("name"));
                System.out.println("Student Email: " + rs.getString("email"));
                System.out.println("Student Study Program: " + rs.getString("studyprogram") + "\n");

                if (!rs.isLast()) {
                    System.out.println("----------------------------\n");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void statistics() throws SQLException {
        System.out.println("Student statistics:");
        HashMap<String, Integer> statistics = studentDAO.statistics();
        for (String key : statistics.keySet()) {
            System.out.println(key + ": " + statistics.get(key));
        }

    }

    public void studentMenu(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("Student Menu");
            System.out.println("1. Add Student");
            System.out.println("2. Update Student");
            System.out.println("3. Ban Student");
            System.out.println("4. Get Students");
            System.out.println("5. Get Student");
            System.out.println("6. Exit");
            System.out.println("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    try {
                        addStudent(scanner);
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        System.out.println("Enter student id: ");
                        String id = scanner.nextLine();
                        if (studentExists(id)) {
                            updateStudent(scanner,id);
                        }
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        System.out.println("Enter student Id: ");
                        String id = scanner.nextLine();
                        if (studentExists(id)) {
                            deleteStudent();
                        }
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        getStudents();
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        System.out.println("Enter student Id or Email: ");
                        String searchTerm = scanner.nextLine();
                        if (studentExists(searchTerm)) {
                            getStudent(searchTerm);
                        }
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }
}
