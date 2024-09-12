package main.java.business;

import main.java.dao.ProfessorDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

public class Professor extends User {
    private String department;
    private ProfessorDAO professorDAO;

    public Professor(String name, String email, String department, Connection connection) {
        super(connection);
        this.setId(generateUuid());
        this.setName(name);
        this.setEmail(email);
        this.department = department;
        this.professorDAO = new ProfessorDAO(connection);
    }

    public Professor(Connection connection) {
        super(connection);
        this.department = "";
        this.professorDAO = new ProfessorDAO(connection);
    }

    private UUID generateUuid() {
        return UUID.randomUUID();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void addProfessor(Scanner scanner) throws SQLException {
        System.out.println("Enter professor name: ");
        setName(scanner.nextLine());
        System.out.println("Enter professor email: ");
        setEmail(scanner.nextLine());
        System.out.println("Enter professor department: ");
        setDepartment(scanner.nextLine());
        setId(generateUuid());
        professorDAO.addProfessor(getId().toString(), getName(), getEmail(), getDepartment());
    }

    public boolean professorExists(String searchTerm) throws SQLException {
        boolean exists = professorDAO.professorExists(searchTerm);
        if (exists) {
            ResultSet rs = professorDAO.getProfessor(searchTerm);
            while (rs.next()) {
                setId(UUID.fromString(rs.getString("id")));
                setName(rs.getString("name"));
                setEmail(rs.getString("email"));
                setDepartment(rs.getString("department"));
            }
        } else {
            System.out.println("Professor does not exist.");
        }
        return exists;
    }

    public void updateProfessor(Scanner scanner, String id) throws SQLException {
        System.out.println("Enter professor name: ");
        setName(scanner.nextLine());
        System.out.println("Enter professor email: ");
        setEmail(scanner.nextLine());
        System.out.println("Enter professor department: ");
        setDepartment(scanner.nextLine());

        professorDAO.updateProfessor(id, getName(), getEmail(), getDepartment());
    }

    public void deleteProfessor() throws SQLException {
        professorDAO.deleteProfessor(getId().toString());
    }

    public void getProfessors() throws SQLException {
        displayData(professorDAO.getAllProfessors());
    }

    public void getProfessor(String searchTerm) throws SQLException {
        ResultSet rs = professorDAO.getProfessor(searchTerm);
        displayData(rs);
    }

    public void displayData(ResultSet rs) {
        try {
            while (rs.next()) {
                System.out.println("Professor ID: " + rs.getString("id"));
                System.out.println("Professor Name: " + rs.getString("name"));
                System.out.println("Professor Email: " + rs.getString("email"));
                System.out.println("Professor Department: " + rs.getString("department") + "\n");

                if (!rs.isLast()) {
                    System.out.println("----------------------------\n");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void professorMenu(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("Professor Menu");
            System.out.println("1. Add Professor");
            System.out.println("2. Update Professor");
            System.out.println("3. Delete Professor");
            System.out.println("4. Get Professors");
            System.out.println("5. Get Professor");
            System.out.println("6. Exit");
            System.out.println("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    try {
                        addProfessor(scanner);
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        System.out.println("Enter professor id: ");
                        String id = scanner.nextLine();
                        if (professorExists(id)) {
                            updateProfessor(scanner, id);
                        }
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        System.out.println("Enter professor id: ");
                        String id = scanner.nextLine();
                        if (professorExists(id)) {
                            deleteProfessor();
                        }
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        getProfessors();
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        System.out.println("Enter professor Id or Email: ");
                        String searchTerm = scanner.nextLine();
                        if (professorExists(searchTerm)) {
                            getProfessor(searchTerm);
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
