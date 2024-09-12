package main.java.dao.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ProfessorDAOInterface {
    void addProfessor(String id, String name, String email, String department) throws SQLException;
    void updateProfessor(String id, String name, String email, String department) throws SQLException;
    void deleteProfessor(String id) throws SQLException;
    ResultSet getProfessor(String searchTerm) throws SQLException;
    ResultSet getAllProfessors() throws SQLException;
    boolean professorExists(String searchTerm) throws SQLException;
}
