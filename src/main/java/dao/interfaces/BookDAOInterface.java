package main.java.dao.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface BookDAOInterface {
    public void addBook(String id, String title, String author, String releaseDate, int pages, String isbn) throws SQLException;
    public void updateBook(String id, String title, String author, String releaseDate, int pages, String isbn) throws SQLException;
    public void deleteBook(String id) throws SQLException;
    public ResultSet getBook(String searchTerm) throws SQLException;
    public ResultSet getAllBooks() throws SQLException;
    public boolean bookExists(String id) throws SQLException;
}
