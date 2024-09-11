package main.java.business;

import main.java.ui.ConsoleUI;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class Document {
    private final Connection connection;

    private UUID id;
    private String title;
    private String author;
    private String releaseDate;
    private int pages;

    public Book book;
    public Magazine magazine;
    public ScientificJournal scientificJournal;
    public UniversityThesis universityThesis;

    public Document(UUID id, String title, String author, String releaseDate, int pages, Connection connection) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        this.pages = pages;
        this.connection = connection;
    }

    public Document(Connection connection){
        this.connection = connection;
        this.book = new Book(connection);
        this.magazine = new Magazine(connection);
        //this.scientificJournal = new ScientificJournal(connection);
        //this.universityThesis = new UniversityThesis(connection);

    }

    public UUID getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getPages() {
        return pages;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void displayDocuments(ConsoleUI.UserRole role) throws SQLException {
        System.out.println("Books:");
        book.getBooks();
        System.out.println("Magazines:");
        magazine.getMagazines();
        if (role == ConsoleUI.UserRole.ADMIN || role == ConsoleUI.UserRole.PROFESSOR) {
            //scientificJournal.getScientificJournals();
            //universityThesis.getUniversityThesis();
        }
    }
}
