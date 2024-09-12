package main.java.dao;

import main.java.dao.interfaces.SciJournalDAOInterface;
import main.java.utils.DateUtils;
import main.java.utils.InputValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScientificJournalDAO implements SciJournalDAOInterface {
    private final Connection connection;
    InputValidator inputValidator = new InputValidator();

    public ScientificJournalDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addScientificJournal(String id, String title, String author, String releaseDate, int pages, String researchField, String editor) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO scientificjournals (id, title, author, releasedate, pages, researchfield, editor) VALUES (?::uuid, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, id);
        ps.setString(2, title);
        ps.setString(3, author);
        ps.setObject(4, DateUtils.fromDateString(releaseDate));
        ps.setInt(5, pages);
        ps.setString(6, researchField);
        ps.setString(7, editor);
        ps.executeUpdate();
        ps.close();
        System.out.println("Scientific journal added successfully");
    }

    @Override
    public void updateScientificJournal(String id, String title, String author, String releaseDate, int pages, String researchField, String editor) throws SQLException {
        if (title.isEmpty() && author.isEmpty() && releaseDate.isEmpty() && pages == 0 && researchField.isEmpty() && editor.isEmpty()) {
            System.out.println("No fields to update.");
            return;
        }

        StringBuilder sql = new StringBuilder("UPDATE scientificjournals SET ");
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
        if (!researchField.isEmpty()) {
            if (!firstField) {
                sql.append(", ");
            }
            sql.append("researchfield = ?");
            firstField = false;
        }
        if (!editor.isEmpty()) {
            if (!firstField) {
                sql.append(", ");
            }
            sql.append("editor = ?");
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
            ps.setString(i++, releaseDate);
        }
        if (pages != 0) {
            ps.setInt(i++, pages);
        }
        if (!researchField.isEmpty()) {
            ps.setString(i++, researchField);
        }
        if (!editor.isEmpty()) {
            ps.setString(i++, editor);
        }

        ps.setString(i, id);
        ps.executeUpdate();
        ps.close();
        System.out.println("Scientific journal updated successfully");
    }

    @Override
    public void deleteScientificJournal(String id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM scientificjournals WHERE id = ?::uuid");
        ps.setString(1, id);
        ps.executeUpdate();
        ps.close();
        System.out.println("Scientific journal deleted successfully");
    }

    @Override
    public ResultSet getScientificJournal(String searchTerm) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            if (inputValidator.validateId(searchTerm)) {
                ps = connection.prepareStatement("SELECT * FROM scientificjournals WHERE id = ?::uuid");
                ps.setObject(1, searchTerm, java.sql.Types.OTHER);
            } else {
                ps = connection.prepareStatement("SELECT * FROM scientificjournals WHERE title = ? OR author = ? OR researchfield = ? OR editor = ?");
                ps.setString(1, searchTerm);
                ps.setString(2, searchTerm);
                ps.setString(3, searchTerm);
                ps.setString(4, searchTerm);
            }

            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }

        return rs;
    }

    @Override
    public ResultSet getAllScientificJournals() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM scientificjournals ORDER BY releasedate DESC");
        return ps.executeQuery();
    }

    @Override
    public boolean scientificJournalExists(String id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM scientificjournals WHERE id = ?::uuid");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        boolean exists = rs.next();
        rs.close();
        ps.close();

        return exists;
    }
}
