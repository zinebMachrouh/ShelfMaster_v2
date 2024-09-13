package main.java.business;

import main.java.dao.MagazineDAO;
import main.java.utils.DateUtils;
import main.java.utils.InputValidator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

public class Magazine extends Document {

    private int number;

    public MagazineDAO magazineDAO;

    public Magazine(UUID id, String title, String author, String releaseDate, int pages, int number, Connection connection) throws SQLException {
        super(id, title, author, releaseDate, pages, connection);
        this.number = number;

        this.magazineDAO = new MagazineDAO(connection);
    }

    public Magazine(Connection connection) throws SQLException {
        super(null, "", "", "", 0, connection);
        this.number = 0;

        this.magazineDAO = new MagazineDAO(connection);
    }

    private UUID generateUuid() {
        return UUID.randomUUID();
    }

    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public void addMagazine(Scanner scanner) throws SQLException {
        String title;
        do {
            System.out.println("Enter magazine title: ");
            title = scanner.nextLine();
            if (!InputValidator.handleTitle(title)) {
                System.out.println("Invalid input. Please enter a valid title.");
            }
        } while (!InputValidator.handleTitle(title));
        setTitle(title);

        String author;
        do {
            System.out.println("Enter magazine author: ");
            author = scanner.nextLine();
            if (!InputValidator.handleAuthor(author)) {
                System.out.println("Invalid input. Please enter a valid author.");
            }
        } while (!InputValidator.handleAuthor(author));
        setAuthor(author);

        String releaseDate;
        do {
            System.out.println("Enter magazine release date (yyyy-mm-dd): ");
            releaseDate = scanner.nextLine();
            if (!DateUtils.handleDate(releaseDate)) {
                System.out.println("Invalid input. Please enter a valid date.");
            }
        } while (!DateUtils.handleDate(releaseDate));
        setReleaseDate(releaseDate);

        int pages;
        do {
            System.out.println("Enter magazine pages: ");
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

        int number;
        do {
            System.out.println("Enter magazine number: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
            number = scanner.nextInt();
            scanner.nextLine();
            if (!InputValidator.handleNumber(number)) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        } while (!InputValidator.handleNumber(number));
        this.number = number;

        setId(generateUuid());

        magazineDAO.addMagazine(getId().toString(), getTitle(), getAuthor(), getReleaseDate(), getPages(), this.number);
    }


    public void updateMagazine(Scanner scanner, String id) throws SQLException {
        System.out.println("Enter magazine title: ");
        if (scanner.hasNextLine()) {
            setTitle(scanner.nextLine());
        } else {
            setTitle("");
        }

        System.out.println("Enter magazine author: ");
        if (scanner.hasNextLine()) {
            setAuthor(scanner.nextLine());
        } else {
            setAuthor("");
        }

        System.out.println("Enter magazine release date: ");
        DateUtils dateUtils = new DateUtils();
        if (scanner.hasNextLine()) {
            setReleaseDate((scanner.nextLine()));
        } else {
            setReleaseDate("");
        }

        magazineDAO.updateMagazine(id, getTitle(), getAuthor(), getReleaseDate(), getPages(), this.number);
    }

    public boolean magazineExists(String id) throws SQLException {
        boolean exists = magazineDAO.magazineExists(id);
        if (exists) {
            ResultSet rs = magazineDAO.getMagazine(id);
            while (rs.next()) {
                displayData(rs);
            }
        } else {
            System.out.println("Magazine does not exist.");
        }
        return exists;
    }

    public void deleteMagazine() throws SQLException {
        magazineDAO.deleteMagazine(getId().toString());
    }

    public void getMagazines() throws SQLException {
        displayData(magazineDAO.getAllMagazines());
    }

    public void getMagazine(String searchTerm) throws SQLException {
        ResultSet rs = magazineDAO.getMagazine(searchTerm);
        displayData(rs);
    }

    public void displayData(ResultSet rs) throws SQLException {
        if (!rs.isBeforeFirst()) {
            System.out.println("No magazines found.");
            return;
        } else {
            while (rs.next()) {
                StringBuilder output = new StringBuilder();
                output.append("ID: ").append(rs.getString("id")).append("\n")
                        .append("Title: ").append(rs.getString("title")).append("\n")
                        .append("Author: ").append(rs.getString("author")).append("\n")
                        .append("Release Date: ").append(rs.getString("releasedate")).append("\n")
                        .append("Pages: ").append(rs.getInt("pages")).append("\n")
                        .append("Number: ").append(rs.getInt("number")).append("\n");

                String borrowedBy = rs.getString("borrowedby");
                String reservedBy = rs.getString("reservedby");

                if (borrowedBy == null && reservedBy == null) {
                    output.append("Status: Available");
                } else if (borrowedBy != null && reservedBy == null) {
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

    public void magazineMenu(Scanner scanner) throws SQLException {
        boolean exit = false;
        while (!exit) {
            System.out.println("Magazine Menu");
            System.out.println("1. Add Magazine");
            System.out.println("2. Update Magazine");
            System.out.println("3. Delete Magazine");
            System.out.println("4. Get All Magazines");
            System.out.println("5. Get Magazine");
            System.out.println("6. Exit");
            System.out.println("Enter choice: ");
            int choice;
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    addMagazine(scanner);
                    break;
                case 2:
                    System.out.println("Enter magazine ID: ");
                    String id = scanner.nextLine();
                    if (magazineExists(id)) {
                        updateMagazine(scanner, id);
                    }
                    break;
                case 3:
                    System.out.println("Enter magazine ID: ");
                    id = scanner.nextLine();
                    if (magazineExists(id)) {
                        deleteMagazine();
                    }
                    break;
                case 4:
                    getMagazines();
                    break;
                case 5:
                    System.out.println("Enter search term: ");
                    String searchTerm = scanner.nextLine();
                    getMagazine(searchTerm);
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid input. Please enter a valid number.");
                    break;
            }
        }
    }
}
