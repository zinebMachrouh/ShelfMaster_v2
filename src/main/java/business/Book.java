package main.java.business;

import main.java.dao.BookDAO;
import main.java.utils.DateUtils;
import main.java.utils.InputValidator;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.UUID;

public class Book extends Document{

    private String isbn;

    public BookDAO bookDAO;

    public Book(UUID id, String title, String author, String releaseDate, int pages, String isbn, Connection connection){
        super(id, title, author, releaseDate, pages, connection);
        this.isbn = isbn;

        this.bookDAO = new BookDAO(connection);
    }

    public Book(Connection connection){
        super(null, "", "", "", 0, connection);
        this.isbn = "";

        this.bookDAO = new BookDAO(connection);
    }

    private UUID generateUuid() {
        return UUID.randomUUID();
    }


    public String getIsbn() {
        return isbn;
    }

    public void addBook(Scanner scanner) throws SQLException {
        String title;
        do {
            System.out.println("Enter book title: ");
            title = scanner.nextLine();
            if (!InputValidator.handleTitle(title)) {
                System.out.println("Invalid input. Please enter a valid title.");
            }
        } while (!InputValidator.handleTitle(title));
        setTitle(title);

        String author;
        do {
            System.out.println("Enter book author: ");
            author = scanner.nextLine();
            if (!InputValidator.handleAuthor(author)) {
                System.out.println("Invalid input. Please enter a valid author.");
            }
        } while (!InputValidator.handleAuthor(author));
        setAuthor(author);

        String releaseDate;
        do {
            System.out.println("Enter book release date (yyyy-mm-dd): ");
            releaseDate = scanner.nextLine();
            if (!DateUtils.handleDate(releaseDate)) {
                System.out.println("Invalid input. Please enter a valid date.");
            }
        } while (!DateUtils.handleDate(releaseDate));
        setReleaseDate(releaseDate);

        int pages;
        do {
            System.out.println("Enter book pages: ");
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

        String isbn;
        do {
            System.out.println("Enter book ISBN: ");
            isbn = scanner.nextLine();
            if (!InputValidator.handleISBN(isbn)) {
                System.out.println("Invalid input. Please enter a valid ISBN.");
            }
        } while (!InputValidator.handleISBN(isbn));
        this.isbn = isbn;

        setId(generateUuid());

        bookDAO.addBook(getId().toString(), getTitle(), getAuthor(), getReleaseDate(), getPages(), this.isbn);

    }


    public void updateBook(Scanner scanner) throws SQLException {
        System.out.println("Enter book title: ");
        if (scanner.hasNextLine()) {
            setTitle(scanner.nextLine());
        }else{
            setTitle(getTitle());
        }

        System.out.println("Enter book author: ");
        if (scanner.hasNextLine()) {
            setAuthor(scanner.nextLine());
        }else {
            setAuthor(getAuthor());
        }

        System.out.println("Enter book release date: ");
        DateUtils dateUtils = new DateUtils();
        if (scanner.hasNextLine()) {
            setReleaseDate(String.valueOf(dateUtils.handleDate(scanner.nextLine())));
        }else{
            setReleaseDate("");
        }

        System.out.println("Enter book pages: ");
        if (scanner.hasNextInt()) {
            setPages(scanner.nextInt());
        }else{
            setPages(getPages());
        }
        scanner.nextLine();

        System.out.println("Enter book ISBN: ");
        if (scanner.hasNextLine()) {
            this.isbn = scanner.nextLine();
        }else{
            this.isbn = "";
        }
        bookDAO.updateBook(getId().toString(), getTitle(), getAuthor(), getReleaseDate(), getPages(), this.isbn);

    }

    public boolean bookExists(String id) throws SQLException {
        boolean exists = bookDAO.bookExists(id);
        if (exists){
            ResultSet rs = bookDAO.getBook(id);
            while (rs.next()){
                setId(UUID.fromString(rs.getString("id")));
                setTitle(rs.getString("title"));
                setAuthor(rs.getString("author"));
                setReleaseDate(rs.getString("releasedate"));
                setPages(rs.getInt("pages"));
                this.isbn = rs.getString("isbn");
            }
        } else {
            System.out.println("Book does not exist.");
        }
        return exists;
    }

    public void deleteBook() throws SQLException {
        bookDAO.deleteBook(getId().toString());
    }

    public void getBooks() throws SQLException {
        displayData(bookDAO.getAllBooks());
    }

    public void getBook(String searchTerm) throws SQLException {
        ResultSet rs = bookDAO.getBook(searchTerm);
        displayData(rs);
    }

    public void displayData(ResultSet rs) throws SQLException {
        if (!rs.isBeforeFirst()) {
            System.out.println("No books found.");
            return;
        }else {
            while (rs.next()) {
                System.out.println("ID: " + rs.getString("id") + "\n" +
                        "Title: " + rs.getString("title") + "\n"
                        + "Author: " + rs.getString("author") + "\n"
                        + "Release Date: " + rs.getString("releasedate") + "\n"
                        + "Pages: " + rs.getInt("pages") + "\n"
                        + "ISBN: " + rs.getString("isbn") + "\n");
            }
        }
    }

    public void bookMenu(Scanner scanner) {
        boolean exit = false;
        while (!exit) {
            System.out.println("Book Menu");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Delete Book");
            System.out.println("4. Get All Books");
            System.out.println("5. Get Book");
            System.out.println("6. Exit");
            System.out.println("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    try {
                        addBook(scanner);
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        System.out.println("Enter book ID: ");
                        String id = scanner.nextLine();
                        if (bookExists(id)) {
                            updateBook(scanner);
                        }
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        System.out.println("Enter book ID: ");
                        String id = scanner.nextLine();
                        if (bookExists(id)) {
                            deleteBook();
                        }
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        getBooks();
                    } catch (SQLException e) {
                        System.err.println("SQL Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        System.out.println("Enter book search term: ");
                        String searchTerm = scanner.nextLine();
                        getBook(searchTerm);
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
