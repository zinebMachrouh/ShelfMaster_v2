package main.java.business;

import main.java.business.Interfaces.LendableInterface;
import main.java.business.Interfaces.ReservableInterface;
import main.java.config.Session;
import main.java.dao.*;
import main.java.ui.ConsoleUI;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class Document  {
    private final Connection connection;

    private UUID id;
    private String title;
    private String author;
    private String releaseDate;
    private int pages;
    private UUID borrowedBy = null;
    private UUID reservedBy = null;

    private final DocumentDAO documentDAO;

    HashMap<String, Document> documents = new HashMap<>();

    public Document(UUID id, String title, String author, String releaseDate, int pages, Connection connection) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        this.pages = pages;
        this.connection = connection;
        this.documentDAO = new DocumentDAO(connection);

    }

    public Document(Connection connection) {
        this.connection = connection;
        this.documentDAO = new DocumentDAO(connection);

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
    public UUID getBorrowedBy() {
        return borrowedBy;
    }

    public UUID getReservedBy() {
        return reservedBy;
    }

    public void setBorrowedBy(UUID borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    public void setReservedBy(UUID reservedBy) {
        this.reservedBy = reservedBy;
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
        new Book(connection).getBooks();
        System.out.println("\n--------------------------------------\n");
        System.out.println("Magazines:");
        new Magazine(connection).getMagazines();
        if (role == ConsoleUI.UserRole.ADMIN || role == ConsoleUI.UserRole.PROFESSOR) {
            System.out.println("\n--------------------------------------\n");
            new ScientificJournal(connection).getScientificJournals();
            System.out.println("\n--------------------------------------\n");
            new UniversityThesis(connection).getTheses();
        }
    }




}
