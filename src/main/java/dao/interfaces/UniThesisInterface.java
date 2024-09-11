package main.java.dao.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface UniThesisInterface {
    public void addUniThesis(String id, String title, String author, String releaseDate, int pages, String university,String fieldOfStudy,int submittedYear) throws SQLException;
    public void updateUniThesis(String id, String title, String author, String releaseDate, int pages, String university,String fieldOfStudy,int submittedYear) throws SQLException;
    public void deleteUniThesis(String id) throws SQLException;
    public ResultSet getUniThesis(String searchTerm) throws SQLException;
    public ResultSet getAllUniThesis() throws SQLException;
    public boolean uniThesisExists(String id) throws SQLException;
}
