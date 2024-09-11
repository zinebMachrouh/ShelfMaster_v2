package main.java.dao.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface MagazineDAOInterface {
    public void addMagazine(String id, String title, String author, String releaseDate, int pages, int number) throws SQLException;
    public void updateMagazine(String id, String title, String author, String releaseDate, int pages, int number) throws SQLException;
    public void deleteMagazine(String id) throws SQLException;
    public ResultSet getMagazine(String searchTerm) throws SQLException;
    public ResultSet getAllMagazines() throws SQLException;
    public boolean magazineExists(String id) throws SQLException;
}
