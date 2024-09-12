package main.java.dao;

import main.java.dao.interfaces.BookDAOInterface;
import main.java.utils.DateUtils;
import main.java.utils.InputValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDAO implements BookDAOInterface {
    private final Connection connection;
    InputValidator inputValidator = new InputValidator();
    public  BookDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addBook(String id, String title, String author, String releaseDate, int pages, String isbn) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO books (id, title, author, releasedate, pages, isbn) VALUES (?::uuid, ?, ?, ?, ?, ?)");
        ps.setString(1, id);
        ps.setString(2, title);
        ps.setString(3, author);
        ps.setObject(4, DateUtils.fromDateString(releaseDate));
        ps.setInt(5, pages);
        ps.setString(6, isbn);
        ps.executeUpdate();
        ps.close();
        System.out.println("Book added successfully");
    }

    @Override
    public void updateBook(String id, String title, String author, String releaseDate, int pages, String isbn) throws SQLException {
        if (title.isEmpty() && author.isEmpty() && releaseDate.isEmpty() && pages == 0 && isbn.isEmpty()) {
            System.out.println("No fields to update.");
            return;
        }

        StringBuilder sql = new StringBuilder("UPDATE books SET ");
        boolean firstField = true;

        if (!title.isEmpty()) {
            sql.append("title = ?");
            firstField = false;
        }
        if (!author.isEmpty()) {
            if (!firstField) {
                sql.append(", ");
            }
            sql.append("author = ?");
            firstField = false;
        }
        if (!releaseDate.isEmpty()) {
            if (!firstField) {
                sql.append(", ");
            }
            sql.append("releasedate = ?");
            firstField = false;
        }
        if (pages != 0) {
            if (!firstField) {
                sql.append(", ");
            }
            sql.append("pages = ?");
            firstField = false;
        }
        if (!isbn.isEmpty()) {
            if (!firstField) {
                sql.append(", ");
            }
            sql.append("isbn = ?");
        }

        sql.append(" WHERE id = ?::uuid");

        PreparedStatement ps = connection.prepareStatement(sql.toString());

        int i = 1;
        if (!title.isEmpty()) {
            ps.setString(i++, title);
        }
        if (!author.isEmpty()) {
            ps.setString(i++, author);
        }
        if (!releaseDate.isEmpty()) {
            ps.setObject(i++, DateUtils.fromDateString(releaseDate));
        }
        if (pages != 0) {
            ps.setInt(i++, pages);
        }
        if (!isbn.isEmpty()) {
            ps.setString(i++, isbn);
        }

        ps.setString(i, id);
        ps.executeUpdate();
        ps.close();
        System.out.println("Book updated successfully");
    }

    @Override
    public void deleteBook(String id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM books WHERE id = ?::uuid");
        ps.setString(1, id);
        ps.executeUpdate();
        ps.close();
        System.out.println("Book deleted successfully");
    }

    @Override
    public ResultSet getBook(String searchTerm) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            if (inputValidator.validateId(searchTerm)) {
                ps = connection.prepareStatement("SELECT * FROM books WHERE id = ?::uuid");
                ps.setObject(1, searchTerm, java.sql.Types.OTHER);
            } else {
                ps = connection.prepareStatement("SELECT * FROM books WHERE title = ? OR author = ? OR isbn = ?");
                ps.setString(1, searchTerm);
                ps.setString(2, searchTerm);
                ps.setString(3, searchTerm);
            }

            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }

        return rs;
    }

    @Override
    public boolean bookExists(String id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM books WHERE id = ?::uuid");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        boolean exists = rs.next();
        rs.close();
        ps.close();

        return exists;
    }

    @Override
    public ResultSet getAllBooks() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM books ORDER BY releasedate DESC");
        return ps.executeQuery();
    }


}
