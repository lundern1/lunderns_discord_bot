package org.example.spillmappe;

import com.zaxxer.hikari.HikariDataSource;
import org.example.MyUtilsMappe.MyUtils;

import java.sql.*;

/**
 * klasse for å håndtere database-spørringer og connections
 */
public class DatabaseHandler {
    private static final String TABLE_NAME = "test";
    private static final String JDBC_URL  = "jdbc:mysql://localhost/discordbot";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD= "";
    static HikariDataSource dataSource;

    /**
     * konstruktør for connection-pool
     */
    public static void initConnection(){
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(JDBC_URL);
        dataSource.setUsername(JDBC_USERNAME);
        dataSource.setPassword(JDBC_PASSWORD);
    }

    /**
     * skriver til database en ny bruker
     * @param id String userid til bruker
     * @param balance int startbeløp
     * @return returner true ny bruker regget, false om eksisterer
     */
    public static boolean skrivTilDB(String id, int balance){
        String insertUserQuery = "INSERT INTO test VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection()){
            if (finnes(connection, id))
                return false;
            try(PreparedStatement statement = connection.prepareStatement(insertUserQuery)){
                statement.setString(1, id);
                statement.setInt(2, balance);
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * sjekker om en bruker finnes i databasen
     * @param con Connection objekt
     * @param id String userID
     * @return returner true om finnes, false om ikke
     */
    static boolean finnes(Connection con, String id) {
        String userExistsQuery = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?;";
        try(PreparedStatement statement = con.prepareStatement(userExistsQuery)) {
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                return true;
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * henter i beløpet en spesifikk bruker har
     * @param id String userID til bruker
     * @return int returnerer beløpet til id bruker
     */
    public static int getBalance(String id){
        int balanceFromDB = -1;
        String getBalanceQuery = "SELECT balance FROM " + TABLE_NAME + " WHERE id = ?;";

        try (Connection connection = dataSource.getConnection()){
            if (!finnes(connection, id))
                return balanceFromDB;

            try(PreparedStatement statement = connection.prepareStatement(getBalanceQuery)){
                statement.setString(1, id);
                ResultSet rs = statement.executeQuery(getBalanceQuery);
                while (rs.next()){
                    balanceFromDB = rs.getInt("balance");
                }
                return balanceFromDB;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * sletter en bruker fra databasen om den eksisterer
     * @param id userID til bruker som skal slettes
     * @return true om bruker ble slettet, false om ikke
     */
    public static boolean deleteBruker(String id){
        String deleteUserQuery = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

        try(Connection con = dataSource.getConnection()) {
            if (!finnes(con, id))
                return false;
            try (PreparedStatement statement = con.prepareStatement(deleteUserQuery)){
                statement.setString(1, id);
                statement.executeUpdate(deleteUserQuery);
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * lukker connection til poolet
     */
    private static void closeDatasource() {
        dataSource.close();
    }

    /**
     * oppdaterer beløpet bruker han på konto
     * @param id String bruker som skal oppdateres
     * @param newBalance int nytt beløp på konto til bruker
     * @return true om beløp ble oppdatert, false om bruker ikke finnes
     */
    static boolean updateBalance(String id, int newBalance){
        String updateBalanceQuery = "UPDATE " + TABLE_NAME + " " + "SET balance = ? WHERE id = ?";
        try(Connection connection = dataSource.getConnection()) {
            if (!finnes(connection, id))
                return false;
            try(PreparedStatement statement = connection.prepareStatement(updateBalanceQuery)) {
                statement.setInt(1, newBalance);
                statement.setString(2, id);
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    }

