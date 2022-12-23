package org.example.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.database.ConnectionHandler.dataSource;
import static org.example.database.ConnectionHandler.existsInTable;

public class LevelHandler {
    public static final String TABLE_LEVEL = "level";

    /**
     * skriver til database en ny bruker i level
     * @param id String userid til bruker
     * @param rank int startrank
     * @param xp int xp start
     * @param messages antal meldinger sendt
     * @return returner true ny bruker regget i level, false om eksisterer
     */
    public static boolean skrivBrukerTilLevel(String id, int rank, int xp, int messages) {
        // sql spørring for å inserte verdier i en tabell
        String insertUserQuery = "INSERT INTO " + TABLE_LEVEL + " VALUES (?, ?, ?, ?)";

        // prøver å få conneciton med database
        try (Connection connection = dataSource.getConnection()) {

            // sjekker om bruker allerede existsInTable
            if (existsInTable(connection, id, TABLE_LEVEL))
                return false;

            // prøver å inserte bruker i database
            try (PreparedStatement statement = connection.prepareStatement(insertUserQuery)) {
                statement.setString(1, id);
                statement.setInt(2, rank);
                statement.setInt(3, xp);
                statement.setInt(4, messages);
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("noe feil med database: " + e.getMessage());
            return false;
        }
    }

    /**
     * henter en verdi fra en rad til bruker
     * @param id bruker som skal hente verdi fra
     * @param row rad som skal hentes
     * @return verdien til raden
     */
    public static int getRowFromLevel(String id, String row){
        int rowFromDB = -1;

        // sql query for å hente rad
        String getRowQuery = "SELECT "+ row +" FROM " + TABLE_LEVEL + " WHERE id = ?;";

        // prøver å få connection med database
        try (Connection connection = dataSource.getConnection()){

            // hvis bruker ikke finnnes, lag bruker
            if (!existsInTable(connection, id, TABLE_LEVEL))
                skrivBrukerTilLevel(id, 0, 0, 0);

            // prøv å gjør spørring
            try(PreparedStatement statement = connection.prepareStatement(getRowQuery)){
                statement.setString(1, id);
                ResultSet rs = statement.executeQuery();

                // looper resultatet
                while (rs.next()){
                    rowFromDB = rs.getInt(row);
                }
                return rowFromDB;
            }
        } catch (SQLException e) {
            System.out.println("noe feil med database: " + e.getMessage());
            return rowFromDB;
        }
    }

    /**
     * oppdaterer raden til level table
     * @param id bruker som skal oppdateres
     * @param newValue ny verdi i raden
     * @param row raden som skal oppdateres
     * @return returnerer true om vellykket, false om ikke
     */
    public static boolean updateRow(String id, int newValue, String row){
        // sql query for å oppdatere raden til bruker
        String updateBalanceQuery = "UPDATE " + TABLE_LEVEL + " " + "SET "+row+" = ? WHERE id = ?";

        // prøver å få connection med database
        try(Connection connection = dataSource.getConnection()) {

            // sjekker om bruker existsInTable i database
            if (!existsInTable(connection, id, TABLE_LEVEL))
                return false;

            // prøver å oppdatere raden til bruker om den eksisterer
            try(PreparedStatement statement = connection.prepareStatement(updateBalanceQuery)) {
                statement.setInt(1, newValue);
                statement.setString(2, id);
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("noe feil med database: " + e.getMessage());
            return false;
        }
    }
}
