package org.example.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.database.ConnectionHandler.dataSource;
import static org.example.database.ConnectionHandler.existsInTable;

public class UserHandler {
    public static final String TABLE_USER = "test";

    /**
     * oppdaterer beløpet bruker han på konto
     * @param id String bruker som skal oppdateres
     * @param newBalance int nytt beløp på konto til bruker
     * @return true om beløp ble oppdatert, false om bruker ikke existsInTable
     */
    public static boolean updateBalance(String id, int newBalance){
        // sql query for å oppdatere balance til bruker
        String updateBalanceQuery = "UPDATE " + TABLE_USER + " " + "SET balance = ? WHERE id = ?";

        // prøver å få connection med database
        try(Connection connection = dataSource.getConnection()) {

            // sjekker om bruker existsInTable i database
            if (!existsInTable(connection, id, TABLE_USER))
                skrivBrukerTilUser(id, 500);

            // prøver å oppdatere balance til bruker om den eksisterer
            try(PreparedStatement statement = connection.prepareStatement(updateBalanceQuery)) {
                statement.setInt(1, newBalance);
                statement.setString(2, id);
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("noe feil med database: " + e.getMessage());
            return false;
        }
    }

    /**
     * skriver til database en ny bruker
     * @param id String userid til bruker
     * @param balance int startbeløp
     * @return returner true ny bruker regget, false om eksisterer
     */
    public static boolean skrivBrukerTilUser(String id, int balance){
        // sql spørring for å inserte verdier i en tabell
        String insertUserQuery = "INSERT INTO "+ TABLE_USER +" VALUES (?, ?)";

        // prøver å få conneciton med database
        try (Connection connection = dataSource.getConnection()){

            // sjekker om bruker allerede existsInTable
            if (existsInTable(connection, id, TABLE_USER))
                return false;

            // prøver å inserte bruker i database
            try(PreparedStatement statement = connection.prepareStatement(insertUserQuery)){
                statement.setString(1, id);
                statement.setInt(2, balance);
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("noe feil med database: " + e.getMessage());
            return false;
        }
    }

    /**
     * henter i beløpet en spesifikk bruker har
     * @param id String userID til bruker
     * @return int returnerer beløpet til id bruker, returnerer -1 om bruker ikke existsInTable
     */
    public static int getBalance(String id){
        int balanceFromDB = -1;

        // sql spørring for å hente balance til bruker
        String getBalanceQuery = "SELECT balance FROM " + TABLE_USER + " WHERE id = ?;";

        // prøver å få connection med database
        try (Connection connection = dataSource.getConnection()){

            // hvis bruker ikke existsInTable, return -1
            if (!existsInTable(connection, id, TABLE_USER))
                skrivBrukerTilUser(id, 500);

            // prøver å få balance fra bruker i database
            try(PreparedStatement statement = connection.prepareStatement(getBalanceQuery)){
                statement.setString(1, id);
                ResultSet rs = statement.executeQuery();

                // få balance fra resultset
                while (rs.next()){
                    balanceFromDB = rs.getInt("balance");
                }
                return balanceFromDB;
            }
        } catch (SQLException e) {
            System.out.println("noe feil med database: " + e.getMessage());
            return balanceFromDB;
        }
    }
}
