package main.java.dao.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ProfessorDAOInterface {
    public void addProfessor(String id, String name, String email) throws SQLException;
    public void updateProfessor(String id, String name, String email) throws SQLException;
    public void deleteProfessor(String id) throws SQLException;
    public ResultSet getProfessor(String searchTerm) throws SQLException;
    public ResultSet getAllProfessors() throws SQLException;
    public boolean professorExists(String id) throws SQLException;
}
