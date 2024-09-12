package main.java.dao;

import main.java.dao.interfaces.UniThesisInterface;
import main.java.utils.DateUtils;
import main.java.utils.InputValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UniThesisDAO implements UniThesisInterface {
    private final Connection connection;
    InputValidator inputValidator = new InputValidator();

    public UniThesisDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addUniThesis(String id, String title, String author, String releaseDate, int pages, String university, String fieldOfStudy, int submittedYear) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO unitheses (id, title, author, releasedate, pages, university, fieldofstudy, submittedyear) VALUES (?::uuid, ?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, id);
        ps.setString(2, title);
        ps.setString(3, author);
        ps.setObject(4, DateUtils.fromDateString(releaseDate));
        ps.setInt(5, pages);
        ps.setString(6, university);
        ps.setString(7, fieldOfStudy);
        ps.setInt(8, submittedYear);
        ps.executeUpdate();
        ps.close();
        System.out.println("University Thesis added successfully");
    }

    @Override
    public void updateUniThesis(String id, String title, String author, String releaseDate, int pages, String university, String fieldOfStudy, int submittedYear) throws SQLException {
        if (title.isEmpty() && author.isEmpty() && releaseDate.isEmpty() && pages == 0 && university.isEmpty() && fieldOfStudy.isEmpty() && submittedYear == 0) {
            System.out.println("No fields to update.");
            return;
        }

        StringBuilder sql = new StringBuilder("UPDATE unitheses SET ");
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
        if (!university.isEmpty()) {
            if (!firstField) {
                sql.append(", ");
            }
            sql.append("university = ?");
            firstField = false;
        }
        if (!fieldOfStudy.isEmpty()) {
            if (!firstField) {
                sql.append(", ");
            }
            sql.append("fieldofstudy = ?");
            firstField = false;
        }
        if (submittedYear != 0) {
            if (!firstField) {
                sql.append(", ");
            }
            sql.append("submittedyear = ?");
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
        if (!university.isEmpty()) {
            ps.setString(i++, university);
        }
        if (!fieldOfStudy.isEmpty()) {
            ps.setString(i++, fieldOfStudy);
        }
        if (submittedYear != 0) {
            ps.setInt(i++, submittedYear);
        }

        ps.setString(i, id);
        ps.executeUpdate();
        ps.close();
        System.out.println("University Thesis updated successfully");
    }

    @Override
    public void deleteUniThesis(String id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM unitheses WHERE id = ?::uuid");
        ps.setString(1, id);
        ps.executeUpdate();
        ps.close();
        System.out.println("University Thesis deleted successfully");
    }

    @Override
    public ResultSet getUniThesis(String searchTerm) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            if (inputValidator.validateId(searchTerm)) {
                ps = connection.prepareStatement("SELECT * FROM unitheses WHERE id = ?::uuid");
                ps.setObject(1, searchTerm, java.sql.Types.OTHER);
            } else {
                ps = connection.prepareStatement("SELECT * FROM unitheses WHERE title = ? OR author = ? OR university = ? OR fieldofstudy = ?");
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
    public ResultSet getAllUniThesis() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM unitheses ORDER BY releasedate DESC");
        return ps.executeQuery();
    }

    @Override
    public boolean uniThesisExists(String id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM unitheses WHERE id = ?::uuid");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        boolean exists = rs.next();
        rs.close();
        ps.close();

        return exists;
    }
}
