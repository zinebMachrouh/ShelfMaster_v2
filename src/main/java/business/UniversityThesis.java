package main.java.business;

import main.java.dao.UniThesisDAO;
import main.java.utils.DateUtils;
import main.java.utils.InputValidator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

public class UniversityThesis extends Document {

    private String university;
    private String fieldOfStudy;
    private int submittedYear;

    public UniThesisDAO uniThesisDAO;

    public UniversityThesis(UUID id, String title, String author, String releaseDate, int pages, String university, String fieldOfStudy, int submittedYear, Connection connection) {
        super(id, title, author, releaseDate, pages, connection);
        this.university = university;
        this.fieldOfStudy = fieldOfStudy;
        this.submittedYear = submittedYear;
        this.uniThesisDAO = new UniThesisDAO(connection);
    }

    public UniversityThesis(Connection connection) {
        super(null, "", "", "", 0, connection);
        this.university = "";
        this.fieldOfStudy = "";
        this.submittedYear = 0;
        this.uniThesisDAO = new UniThesisDAO(connection);
    }

    private UUID generateUuid() {
        return UUID.randomUUID();
    }

    public String getUniversity() {
        return university;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public int getSubmittedYear() {
        return submittedYear;
    }

    public void addThesis(Scanner scanner) throws SQLException {
        String title;
        do {
            System.out.println("Enter thesis title: ");
            title = scanner.nextLine();
            if (!InputValidator.handleTitle(title)) {
                System.out.println("Invalid input. Please enter a valid title.");
            }
        } while (!InputValidator.handleTitle(title));
        setTitle(title);

        String author;
        do {
            System.out.println("Enter thesis author: ");
            author = scanner.nextLine();
            if (!InputValidator.handleAuthor(author)) {
                System.out.println("Invalid input. Please enter a valid author.");
            }
        } while (!InputValidator.handleAuthor(author));
        setAuthor(author);

        String releaseDate;
        do {
            System.out.println("Enter thesis release date (yyyy-mm-dd): ");
            releaseDate = scanner.nextLine();
            if (!DateUtils.handleDate(releaseDate)) {
                System.out.println("Invalid input. Please enter a valid date.");
            }
        } while (!DateUtils.handleDate(releaseDate));
        setReleaseDate(releaseDate);

        int pages;
        do {
            System.out.println("Enter thesis pages: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number of pages.");
                scanner.next();
            }
            pages = scanner.nextInt();
            scanner.nextLine();
            if (!InputValidator.handleNumber(pages)) {
                System.out.println("Invalid input. Please enter a valid number of pages.");
            }
        } while (!InputValidator.handleNumber(pages));
        setPages(pages);

        System.out.println("Enter university: ");
        university = scanner.nextLine();
        System.out.println("Enter field of study: ");
        fieldOfStudy = scanner.nextLine();
        System.out.println("Enter submitted year: ");
        submittedYear = scanner.nextInt();
        scanner.nextLine();

        setId(generateUuid());

        uniThesisDAO.addUniThesis(getId().toString(), getTitle(), getAuthor(), getReleaseDate(), getPages(), university, fieldOfStudy, submittedYear);
    }

    public void updateThesis(Scanner scanner) throws SQLException {
        System.out.println("Enter thesis title: ");
        if (scanner.hasNextLine()) {
            setTitle(scanner.nextLine());
        } else {
            setTitle(getTitle());
        }

        System.out.println("Enter thesis author: ");
        if (scanner.hasNextLine()) {
            setAuthor(scanner.nextLine());
        } else {
            setAuthor(getAuthor());
        }

        System.out.println("Enter thesis release date: ");
        if (scanner.hasNextLine()) {
            setReleaseDate(scanner.nextLine());
        } else {
            setReleaseDate(getReleaseDate());
        }

        System.out.println("Enter thesis pages: ");
        if (scanner.hasNextInt()) {
            setPages(scanner.nextInt());
        } else {
            setPages(getPages());
        }
        scanner.nextLine();

        System.out.println("Enter university: ");
        if (scanner.hasNextLine()) {
            university = scanner.nextLine();
        } else {
            university = getUniversity();
        }

        System.out.println("Enter field of study: ");
        if (scanner.hasNextLine()) {
            fieldOfStudy = scanner.nextLine();
        } else {
            fieldOfStudy = getFieldOfStudy();
        }

        System.out.println("Enter submitted year: ");
        if (scanner.hasNextInt()) {
            submittedYear = scanner.nextInt();
        } else {
            submittedYear = getSubmittedYear();
        }
        scanner.nextLine();

        uniThesisDAO.updateUniThesis(getId().toString(), getTitle(), getAuthor(), getReleaseDate(), getPages(), university, fieldOfStudy, submittedYear);
    }

    public boolean thesisExists(String id) throws SQLException {
        boolean exists = uniThesisDAO.uniThesisExists(id);
        if (exists) {
            ResultSet rs = uniThesisDAO.getUniThesis(id);
            while (rs.next()) {
                setId(UUID.fromString(rs.getString("id")));
                setTitle(rs.getString("title"));
                setAuthor(rs.getString("author"));
                setReleaseDate(rs.getString("releasedate"));
                setPages(rs.getInt("pages"));
                this.university = rs.getString("university");
                this.fieldOfStudy = rs.getString("fieldofstudy");
                this.submittedYear = rs.getInt("submittedyear");
            }
        } else {
            System.out.println("University Thesis does not exist.");
        }
        return exists;
    }

    public void deleteThesis() throws SQLException {
        uniThesisDAO.deleteUniThesis(getId().toString());
    }

    public void getTheses() throws SQLException {
        displayData(uniThesisDAO.getAllUniThesis());
    }

    public void getThesis(String searchTerm) throws SQLException {
        ResultSet rs = uniThesisDAO.getUniThesis(searchTerm);
        displayData(rs);
    }

    public void displayData(ResultSet rs) throws SQLException {
        if (!rs.isBeforeFirst()) {
            System.out.println("No university theses found.");
            return;
        } else {
            while (rs.next()) {
                StringBuilder output = new StringBuilder();
                output.append("ID: ").append(rs.getString("id")).append("\n")
                        .append("Title: ").append(rs.getString("title")).append("\n")
                        .append("Author: ").append(rs.getString("author")).append("\n")
                        .append("Release Date: ").append(rs.getString("releasedate")).append("\n")
                        .append("Pages: ").append(rs.getInt("pages")).append("\n")
                        .append("University: ").append(rs.getString("university")).append("\n")
                        .append("Field of Study: ").append(rs.getString("fieldofstudy")).append("\n")
                        .append("Submitted Year: ").append(rs.getInt("submittedyear")).append("\n");

                String borrowedBy = rs.getString("borrowedby");
                String reservedBy = rs.getString("reservedby");

                if (borrowedBy == null && reservedBy == null) {
                    output.append("Status: Available");
                } else if (borrowedBy != null) {
                    output.append("Status: Borrowed");
                } else {
                    output.append("Status: Reserved");
                }

                System.out.println(output.toString());

                if (!rs.isLast()) {
                    System.out.println("----------------------------\n");
                }
            }
        }
    }


    public void thesisMenu(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("University Thesis Menu");
            System.out.println("1. Add Thesis");
            System.out.println("2. Update Thesis");
            System.out.println("3. Delete Thesis");
            System.out.println("4. Get All Theses");
            System.out.println("5. Get Thesis");
            System.out.println("6. Exit");
            System.out.println("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    try {
                        addThesis(scanner);
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        System.out.println("Enter thesis ID: ");
                        String id = scanner.nextLine();
                        if (thesisExists(id)) {
                            updateThesis(scanner);
                        }
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        System.out.println("Enter thesis ID: ");
                        String id = scanner.nextLine();
                        if (thesisExists(id)) {
                            deleteThesis();
                        }
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        getTheses();
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        System.out.println("Enter thesis search term: ");
                        String searchTerm = scanner.nextLine();
                        getThesis(searchTerm);
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
