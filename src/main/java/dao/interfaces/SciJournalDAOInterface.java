package main.java.dao.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SciJournalDAOInterface {
    public void addScientificJournal(String id, String title, String author, String releaseDate, int pages, String researchField, String editor) throws SQLException;
    public void updateScientificJournal(String id, String title, String author, String releaseDate, int pages, String researchField,String editor) throws SQLException;
    public void deleteScientificJournal(String id) throws SQLException;
    public ResultSet getScientificJournal(String searchTerm) throws SQLException;
    public ResultSet getAllScientificJournals() throws SQLException;
    public boolean scientificJournalExists(String id) throws SQLException;
}
