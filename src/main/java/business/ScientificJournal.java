package main.java.business;

import main.java.dao.ScientificJournalDAO;
import main.java.utils.DateUtils;
import main.java.utils.InputValidator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

public class ScientificJournal extends Document {

    private String researchField;
    private String editor;
    public ScientificJournalDAO scientificJournalDAO;

    public ScientificJournal(UUID id, String title, String author, String releaseDate, int pages, String researchField, String editor, Connection connection) {
        super(id, title, author, releaseDate, pages, connection);
        this.researchField = researchField;
        this.editor = editor;
        this.scientificJournalDAO = new ScientificJournalDAO(connection);
    }

    public ScientificJournal(Connection connection) {
        super(null, "", "", "", 0, connection);
        this.researchField = "";
        this.editor = "";
        this.scientificJournalDAO = new ScientificJournalDAO(connection);
    }

    private UUID generateUuid() {
        return UUID.randomUUID();
    }

    public String getResearchField() {
        return researchField;
    }

    public void addScientificJournal(Scanner scanner) throws SQLException {
        String title;
        do {
            System.out.println("Enter journal title: ");
            title = scanner.nextLine();
            if (!InputValidator.handleTitle(title)) {
                System.out.println("Invalid input. Please enter a valid title.");
            }
        } while (!InputValidator.handleTitle(title));
        setTitle(title);

        String author;
        do {
            System.out.println("Enter journal author: ");
            author = scanner.nextLine();
            if (!InputValidator.handleAuthor(author)) {
                System.out.println("Invalid input. Please enter a valid author.");
            }
        } while (!InputValidator.handleAuthor(author));
        setAuthor(author);

        String releaseDate;
        do {
            System.out.println("Enter journal release date (yyyy-mm-dd): ");
            releaseDate = scanner.nextLine();
            if (!DateUtils.handleDate(releaseDate)) {
                System.out.println("Invalid input. Please enter a valid date.");
            }
        } while (!DateUtils.handleDate(releaseDate));
        setReleaseDate(releaseDate);

        int pages;
        do {
            System.out.println("Enter journal pages: ");
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

        String researchField;
        do {
            System.out.println("Enter research field: ");
            researchField = scanner.nextLine();
            if (!InputValidator.handleAuthor(researchField)) {
                System.out.println("Invalid input. Please enter a valid research field.");
            }
        } while (!InputValidator.handleAuthor(researchField));
        this.researchField = researchField;

        String editor;
        do {
            System.out.println("Enter editor: ");
            editor = scanner.nextLine();
            if (!InputValidator.handleAuthor(editor)) {
                System.out.println("Invalid input. Please enter a valid editor.");
            }
        } while (!InputValidator.handleAuthor(editor));
        this.editor = editor;

        setId(generateUuid());

        scientificJournalDAO.addScientificJournal(getId().toString(), getTitle(), getAuthor(), getReleaseDate(), getPages(), this.researchField, this.editor);
    }

    public void updateScientificJournal(Scanner scanner, String id) throws SQLException {
        System.out.println("Enter journal title: ");
        if (scanner.hasNextLine()) {
            setTitle(scanner.nextLine());
        } else {
            setTitle(getTitle());
        }

        System.out.println("Enter journal author: ");
        if (scanner.hasNextLine()) {
            setAuthor(scanner.nextLine());
        } else {
            setAuthor(getAuthor());
        }

        System.out.println("Enter journal release date: ");
        DateUtils dateUtils = new DateUtils();
        if (scanner.hasNextLine()) {
            setReleaseDate(String.valueOf(dateUtils.handleDate(scanner.nextLine())));
        } else {
            setReleaseDate("");
        }

        System.out.println("Enter journal pages: ");
        if (scanner.hasNextInt()) {
            setPages(scanner.nextInt());
        } else {
            setPages(getPages());
        }
        scanner.nextLine();

        System.out.println("Enter research field: ");
        if (scanner.hasNextLine()) {
            this.researchField = scanner.nextLine();
        } else {
            this.researchField = "";
        }

        System.out.println("Enter editor: ");
        if (scanner.hasNextLine()) {
            this.editor = scanner.nextLine();
        } else {
            this.editor = "";
        }

        scientificJournalDAO.updateScientificJournal(id, getTitle(), getAuthor(), getReleaseDate(), getPages(), this.researchField, this.editor);
    }

    public boolean scientificJournalExists(String id) throws SQLException {
        boolean exists = scientificJournalDAO.scientificJournalExists(id);
        if (exists) {
            ResultSet rs = scientificJournalDAO.getScientificJournal(id);
            while (rs.next()) {
                setId(UUID.fromString(rs.getString("id")));
                setTitle(rs.getString("title"));
                setAuthor(rs.getString("author"));
                setReleaseDate(rs.getString("releasedate"));
                setPages(rs.getInt("pages"));
                this.researchField = rs.getString("researchfield");
                this.editor = rs.getString("editor");
            }
        } else {
            System.out.println("Scientific journal does not exist.");
        }
        return exists;
    }

    public void deleteScientificJournal() throws SQLException {
        scientificJournalDAO.deleteScientificJournal(getId().toString());
    }

    public void getScientificJournals() throws SQLException {
        displayData(scientificJournalDAO.getAllScientificJournals());
    }

    public void getScientificJournal(String searchTerm) throws SQLException {
        ResultSet rs = scientificJournalDAO.getScientificJournal(searchTerm);
        displayData(rs);
    }

    public void displayData(ResultSet rs) throws SQLException {
        if (!rs.isBeforeFirst()) {
            System.out.println("No scientific journals found.");
            return;
        } else {
            while (rs.next()) {
                StringBuilder output = new StringBuilder();
                output.append("ID: ").append(rs.getString("id")).append("\n")
                        .append("Title: ").append(rs.getString("title")).append("\n")
                        .append("Author: ").append(rs.getString("author")).append("\n")
                        .append("Release Date: ").append(rs.getString("releasedate")).append("\n")
                        .append("Pages: ").append(rs.getInt("pages")).append("\n")
                        .append("Research Field: ").append(rs.getString("researchfield")).append("\n")
                        .append("Editor: ").append(rs.getString("editor")).append("\n");

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

    public void journalMenu(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("Scientific Journal Menu");
            System.out.println("1. Add Scientific Journal");
            System.out.println("2. Update Scientific Journal");
            System.out.println("3. Delete Scientific Journal");
            System.out.println("4. Get All Scientific Journals");
            System.out.println("5. Get Scientific Journal");
            System.out.println("6. Exit");
            System.out.println("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    try {
                        addScientificJournal(scanner);
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        System.out.println("Enter journal ID: ");
                        String id = scanner.nextLine();
                        if (scientificJournalExists(id)) {
                            updateScientificJournal(scanner, id);
                        }
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        System.out.println("Enter journal ID: ");
                        String id = scanner.nextLine();
                        if (scientificJournalExists(id)) {
                            deleteScientificJournal();
                        }
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        getScientificJournals();
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        System.out.println("Enter journal search term: ");
                        String searchTerm = scanner.nextLine();
                        getScientificJournal(searchTerm);
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
