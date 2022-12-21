package org.example.database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

/**
 * klasse for å håndtere database-spørringer og connections
 */
public class DatabaseHandler {
    private static final String TABLE_NAME = "test";
    private static final String JDBC_URL  = "jdbc:mysql://localhost/discordbot";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD= "";
    public static HikariDataSource dataSource;

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
        // sql spørring for å inserte verdier i en tabell
        String insertUserQuery = "INSERT INTO "+ TABLE_NAME +" VALUES (?, ?)";

        // prøver å få conneciton med database
        try (Connection connection = dataSource.getConnection()){

            // sjekker om bruker allerede finnes
            if (finnes(connection, id))
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
     * sjekker om en bruker finnes i databasen
     * @param con Connection objekt
     * @param id String userID
     * @return returner true om finnes, false om ikke
     */
    public static boolean finnes(Connection con, String id) {
        // sql spørring for å hente bruker fra tabell
        String userExistsQuery = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?;";

        // prøver å kjøre query
        try(PreparedStatement statement = con.prepareStatement(userExistsQuery)) {
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();

            // hvis rs har next, så finnes bruker
            if (rs.next())
                return true;
            return false;
        } catch (SQLException e){
            System.out.println("noe feil med database: " + e.getMessage());
            return false;
        }
    }

    /**
     * henter i beløpet en spesifikk bruker har
     * @param id String userID til bruker
     * @return int returnerer beløpet til id bruker, returnerer -1 om bruker ikke finnes
     */
    public static int getBalance(String id){
        int balanceFromDB = -1;

        // sql spørring for å hente balance til bruker
        String getBalanceQuery = "SELECT balance FROM " + TABLE_NAME + " WHERE id = ?;";

        // prøver å få connection med database
        try (Connection connection = dataSource.getConnection()){

            // hvis bruker ikke finnes, return -1
            if (!finnes(connection, id))
                return balanceFromDB;

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

    /**
     * sletter en bruker fra databasen om den eksisterer
     * @param id userID til bruker som skal slettes
     * @return true om bruker ble slettet, false om ikke
     */
    public static boolean deleteBruker(String id){
        // sql query for å slette bruker fra tabell
        String deleteUserQuery = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

        // prøver å få connection med database
        try(Connection con = dataSource.getConnection()) {

            // sjekker om bruker finnes i database
            if (!finnes(con, id))
                return false;

            // hvis bruker finnes, prøv å slett fra database
            try (PreparedStatement statement = con.prepareStatement(deleteUserQuery)){
                statement.setString(1, id);
                statement.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("noe feil med database: " + e.getMessage());
            return false;
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
    public static boolean updateBalance(String id, int newBalance){
        // sql query for å oppdatere balance til bruker
        String updateBalanceQuery = "UPDATE " + TABLE_NAME + " " + "SET balance = ? WHERE id = ?";

        // prøver å få connection med database
        try(Connection connection = dataSource.getConnection()) {

            // sjekker om bruker finnes i database
            if (!finnes(connection, id))
                return false;

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
}

