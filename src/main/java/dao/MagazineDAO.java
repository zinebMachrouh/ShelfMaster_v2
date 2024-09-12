package main.java.dao;

import main.java.dao.interfaces.MagazineDAOInterface;
import main.java.utils.DateUtils;
import main.java.utils.InputValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MagazineDAO implements MagazineDAOInterface {
    private final Connection connection;

    InputValidator inputValidator = new InputValidator();
    public MagazineDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addMagazine(String id, String title, String author, String releaseDate, int pages, int number) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO magazines (id, title, author, releasedate, pages, number) VALUES (?::uuid, ?, ?, ?, ?, ?)");
        ps.setString(1, id);
        ps.setString(2, title);
        ps.setString(3, author);
        ps.setObject(4, DateUtils.fromDateString(releaseDate));
        ps.setInt(5, pages);
        ps.setInt(6, number);
        ps.executeUpdate();
        ps.close();
        System.out.println("Magazine added successfully");
    }

    @Override
    public void updateMagazine(String id, String title, String author, String releaseDate, int pages, int number) throws SQLException {
        if (title.isEmpty() && author.isEmpty() && releaseDate.isEmpty() && pages == 0 && number == 0) {
            System.out.println("No fields to update.");
            return;
        }

        StringBuilder sql = new StringBuilder("UPDATE magazines SET ");
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
        if (number != 0) {
            if (!firstField) {
                sql.append(", ");
            }
            sql.append("number = ?");
            firstField = false;
        }
        sql.append(" WHERE id = ?::uuid");

        PreparedStatement ps = connection.prepareStatement(sql.toString());

        int i = 1;
        if(!title.isEmpty()) {
            ps.setString(i++, title);
        }
        if(!author.isEmpty()) {
            ps.setString(i++, author);
        }
        if(!releaseDate.isEmpty()) {
            ps.setString(i++, releaseDate);
        }
        if(pages != 0) {
            ps.setInt(i++, pages);
        }
        if(number != 0) {
            ps.setInt(i++, number);
        }

        ps.setString(i, id);
        ps.executeUpdate();
        ps.close();
        System.out.println("Magazine updated successfully");
    }

    @Override
    public void deleteMagazine(String id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM magazines WHERE id = ?::uuid");
        ps.setString(1, id);
        ps.executeUpdate();
        ps.close();
        System.out.println("Magazine deleted successfully");
    }

    @Override
    public ResultSet getMagazine(String searchTerm) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            if (inputValidator.validateId(searchTerm)) {
                ps = connection.prepareStatement("SELECT * FROM magazines WHERE id = ?::uuid");
                ps.setObject(1, searchTerm, java.sql.Types.OTHER);
            } else {
                ps = connection.prepareStatement("SELECT * FROM magazines WHERE title = ? OR author = ? OR number = ?");
                ps.setString(1, searchTerm);
                ps.setString(2, searchTerm);
                if (inputValidator.validateNumber(searchTerm)) {
                    ps.setInt(3, Integer.parseInt(searchTerm));
                } else {
                    ps.setInt(3, 0);
                }
            }

            rs = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return rs;
    }

    @Override
    public ResultSet getAllMagazines() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM magazines ORDER BY releasedate DESC");
        return ps.executeQuery();

    }

    @Override
    public boolean magazineExists(String id) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM magazines WHERE id = ?::uuid");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        boolean exists = rs.next();
        rs.close();
        ps.close();

        return exists;
    }


}
