package main.java.dao.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface StudentDAOInterface {
    public void addStudent(String id,String name, String email, String studyProgram) throws SQLException;
    public void updateStudent(String id,String name, String email, String studyProgram) throws SQLException;
    public void deleteStudent(String id) throws SQLException;
    public ResultSet getStudent(String searchTerm) throws SQLException;
    public ResultSet getAllStudents() throws SQLException;
    public boolean studentExists(String id) throws SQLException;
}
