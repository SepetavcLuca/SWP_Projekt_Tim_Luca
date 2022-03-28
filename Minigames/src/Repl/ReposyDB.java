package Repl;

import Models.*;

import java.sql.SQLException;

public interface ReposyDB {
    void open() throws SQLException;
    void close() throws SQLException;

    boolean register(User user) throws SQLException;

    boolean checkIfUserExists(String username) throws SQLException;

    boolean login(String username, String password) throws SQLException;
}
