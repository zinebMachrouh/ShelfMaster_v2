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

public class Document implements LendableInterface, ReservableInterface {
    private final Connection connection;

    private UUID id;
    private String title;
    private String author;
    private String releaseDate;
    private int pages;
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

    public void getAllDocuments() throws SQLException {
        Session session = Session.getInstance();
        String role = session.getRole();

        BookDAO bookDAO = new BookDAO(connection);
        MagazineDAO magazineDAO = new MagazineDAO(connection);

        loadBooks(bookDAO);
        loadMagazines(magazineDAO);

        if ("admin".equalsIgnoreCase(role) || "professor".equalsIgnoreCase(role)) {
            ScientificJournalDAO scientificJournalDAO = new ScientificJournalDAO(connection);
            UniThesisDAO uniThesisDAO = new UniThesisDAO(connection);

            loadScientificJournals(scientificJournalDAO);
            loadUniversityTheses(uniThesisDAO);
        }
    }

    private void loadBooks(BookDAO bookDAO) throws SQLException {
        ResultSet rsBooks = bookDAO.getAllBooks();
        while (rsBooks.next()) {
            String id = rsBooks.getString("id");
            Book bookDocument = new Book(connection);
            bookDocument.setId(UUID.fromString(id));
            bookDocument.setTitle(rsBooks.getString("title"));
            bookDocument.setAuthor(rsBooks.getString("author"));
            bookDocument.setReleaseDate(rsBooks.getString("releasedate"));
            bookDocument.setPages(rsBooks.getInt("pages"));
            bookDocument.setIsbn(rsBooks.getString("isbn"));
            documents.put(id, bookDocument);
        }
        rsBooks.close();
    }

    private void loadMagazines(MagazineDAO magazineDAO) throws SQLException {
        ResultSet rsMagazines = magazineDAO.getAllMagazines();
        while (rsMagazines.next()) {
            String id = rsMagazines.getString("id");
            Magazine magazineDocument = new Magazine(connection);
            magazineDocument.setId(UUID.fromString(id));
            magazineDocument.setTitle(rsMagazines.getString("title"));
            magazineDocument.setAuthor(rsMagazines.getString("author"));
            magazineDocument.setReleaseDate(rsMagazines.getString("releasedate"));
            magazineDocument.setPages(rsMagazines.getInt("pages"));
            magazineDocument.setNumber(rsMagazines.getInt("number"));
            documents.put(id, magazineDocument);
        }
        rsMagazines.close();
    }

    private void loadScientificJournals(ScientificJournalDAO scientificJournalDAO) throws SQLException {
        ResultSet rsScientificJournals = scientificJournalDAO.getAllScientificJournals();
        while (rsScientificJournals.next()) {
            String id = rsScientificJournals.getString("id");
            ScientificJournal journalDocument = new ScientificJournal(connection);
            journalDocument.setId(UUID.fromString(id));
            journalDocument.setTitle(rsScientificJournals.getString("title"));
            journalDocument.setAuthor(rsScientificJournals.getString("author"));
            journalDocument.setReleaseDate(rsScientificJournals.getString("releasedate"));
            journalDocument.setPages(rsScientificJournals.getInt("pages"));
            journalDocument.setResearchField(rsScientificJournals.getString("researchfield"));
            journalDocument.setEditor(rsScientificJournals.getString("editor"));
            documents.put(id, journalDocument);
        }
        rsScientificJournals.close();
    }

    private void loadUniversityTheses(UniThesisDAO uniThesisDAO) throws SQLException {
        ResultSet rsUniversityTheses = uniThesisDAO.getAllUniThesis();
        while (rsUniversityTheses.next()) {
            String id = rsUniversityTheses.getString("id");
            UniversityThesis thesisDocument = new UniversityThesis(connection);
            thesisDocument.setId(UUID.fromString(id));
            thesisDocument.setTitle(rsUniversityTheses.getString("title"));
            thesisDocument.setAuthor(rsUniversityTheses.getString("author"));
            thesisDocument.setReleaseDate(rsUniversityTheses.getString("releasedate"));
            thesisDocument.setPages(rsUniversityTheses.getInt("pages"));
            thesisDocument.setUniversity(rsUniversityTheses.getString("university"));
            thesisDocument.setFieldOfStudy(rsUniversityTheses.getString("fieldofstudy"));
            thesisDocument.setSubmittedYear(rsUniversityTheses.getInt("submittedyear"));
            documents.put(id, thesisDocument);
        }
        rsUniversityTheses.close();
    }

    public void getDocument(String id) throws SQLException {
        getAllDocuments();
        Session session = Session.getInstance();
        String userRole = session.getRole();

        Document document = documents.get(id);

        if (document != null) {
            System.out.println("Title: " + document.getTitle());
            System.out.println("Author: " + document.getAuthor());
            System.out.println("Release Date: " + document.getReleaseDate());
            System.out.println("Pages: " + document.getPages());
            if (document instanceof Book) {
                Book book = (Book) document;
                System.out.println("ISBN: " + book.getIsbn());
            } else if (document instanceof Magazine) {
                Magazine magazine = (Magazine) document;
                System.out.println("Number: " + magazine.getNumber());
            }
            if (userRole.equals("admin") || userRole.equals("professor")) {
                if (document instanceof ScientificJournal) {
                    ScientificJournal journal = (ScientificJournal) document;
                    System.out.println("Research Field: " + journal.getResearchField());
                    System.out.println("Editor: " + journal.getEditor());
                } else if (document instanceof UniversityThesis) {
                    UniversityThesis thesis = (UniversityThesis) document;
                    System.out.println("University: " + thesis.getUniversity());
                    System.out.println("Field of Study: " + thesis.getFieldOfStudy());
                    System.out.println("Submitted Year: " + thesis.getSubmittedYear());
                }
            }
        } else {
            System.out.println("Document not found.");
        }
    }

    public void lendDocument(Scanner scanner, String documentId) throws SQLException {
        Session session = Session.getInstance();
        String userId = session.getId().toString();
        String userRole = session.getRole();

        // Check borrow limit if user is a student
        if ("student".equalsIgnoreCase(userRole)) {
            ResultSet rs = documentDAO.getBorrowLimit(userId);
            if (rs.next()) {
                int borrowLimit = rs.getInt("borrowLimit");
                if (borrowLimit <= 0) {
                    System.out.println("You have reached your borrow limit. Cannot borrow more documents.");
                    return;
                }
            }
        }

        // Check document status
        ResultSet rs = documentDAO.checkDocumentStatus(documentId);
        if (rs.next()) {
            String borrowedBy = rs.getString("borrowedBy");
            String reservedBy = rs.getString("reservedBy");

            if (borrowedBy == null) {  // Document is available to lend
                documentDAO.lendDocument(documentId, userId);
                System.out.println("Document successfully borrowed.");

                if ("student".equalsIgnoreCase(userRole)) {
                    documentDAO.updateBorrowLimit(userId, -1);
                }
            } else if (borrowedBy.equals(userId)) {  // Document is borrowed by the same user
                System.out.println("You have already borrowed this document.");
                System.out.println("Do you want to return it? (yes/no):");
                String userInput = scanner.nextLine();
                if ("yes".equalsIgnoreCase(userInput)) {
                    returnDocument(documentId);
                } else {
                    System.out.println("Operation canceled.");
                }
            } else if (reservedBy == null) {  // Document is borrowed but not reserved
                documentDAO.reserveDocument(documentId, userId);
                System.out.println("Document is currently borrowed. Reservation made.");
            } else if (reservedBy.equals(userId)) {  // Document is reserved by the same user
                System.out.println("Document is already reserved by you. Do you want to cancel the reservation? (yes/no):");
                String userInput = scanner.nextLine();
                if ("yes".equalsIgnoreCase(userInput)) {
                    documentDAO.cancelReservation(documentId);
                    System.out.println("Reservation canceled.");
                }
            } else {  // Document is reserved by another user
                System.out.println("Document is currently reserved by another user. Cannot borrow.");
            }
        }
    }
    public void returnDocument(String documentId) throws SQLException {
        Session session = Session.getInstance();
        String userId = session.getId().toString();
        String userRole = session.getRole();

        ResultSet rs = documentDAO.checkDocumentStatus(documentId);
        if (rs.next()) {
            String reservedBy = rs.getString("reservedBy");

            if (reservedBy != null) {  // There is a reservation
                documentDAO.lendDocument(documentId, reservedBy);
                System.out.println("Document returned and lent to the next reserved user.");
            } else {  // No reservation
                documentDAO.returnDocument(documentId);
                System.out.println("Document successfully returned.");
            }

            if ("student".equalsIgnoreCase(userRole)) {
                documentDAO.updateBorrowLimit(userId, 1);
            }
        }
    }
    public void reserveDocument(Scanner scanner, String documentId) throws SQLException {
        Session session = Session.getInstance();
        String userId = session.getId().toString();
        String userRole = session.getRole();

        ResultSet rs = documentDAO.checkDocumentStatus(documentId);
        if (rs.next()) {
            String borrowedBy = rs.getString("borrowedBy");
            String reservedBy = rs.getString("reservedBy");

            if (borrowedBy == null) { // Document is available to borrow
                System.out.println("The document is currently available. Do you want to borrow it instead of reserving? (yes/no):");
                String userInput = scanner.nextLine();
                if ("yes".equalsIgnoreCase(userInput)) {
                    documentDAO.lendDocument(documentId, userId);
                    System.out.println("Document successfully borrowed.");
                    if ("student".equalsIgnoreCase(userRole)) {
                        documentDAO.updateBorrowLimit(userId, -1);
                    }
                } else {
                    System.out.println("Operation canceled. No reservation made.");
                }
            } else if (borrowedBy != null && borrowedBy.equals(userId)) { // User has already borrowed the document
                System.out.println("You have already borrowed this document.");
                System.out.println("Do you want to return it instead of reserving? (yes/no):");
                String userInput = scanner.nextLine();
                if ("yes".equalsIgnoreCase(userInput)) {
                    returnDocument(documentId);
                } else {
                    System.out.println("Cannot reserve a document that you already have borrowed.");
                }
            } else if (reservedBy == null) { // Document is borrowed but not reserved
                documentDAO.reserveDocument(documentId, userId);
                System.out.println("Document is currently borrowed. Reservation made.");
            } else if (reservedBy.equals(userId)) { // Document is already reserved by the same user
                System.out.println("You have already reserved this document.");
            } else { // Document is reserved by another user
                System.out.println("Document is already reserved by another user.");
            }
        }
    }
    public void cancelReservation(String documentId) throws SQLException {
        Session session = Session.getInstance();
        String userId = session.getId().toString();

        ResultSet rs = documentDAO.checkDocumentStatus(documentId);
        if (rs.next()) {
            String reservedBy = rs.getString("reservedBy");

            if (reservedBy != null && reservedBy.equals(userId)) {  // Document is reserved by this user
                documentDAO.cancelReservation(documentId);
                System.out.println("Reservation successfully canceled.");
            } else if (reservedBy == null) {
                System.out.println("No reservation found for this document.");
            } else {
                System.out.println("You do not have permission to cancel this reservation.");
            }
        }
    }

}
