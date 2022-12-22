package org.example.database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

/**
 * klasse for å håndtere database-spørringer og connections
 */
public class DatabaseHandler {
    public static final String TABLE_USER = "test";
    public static final String TABLE_LEVEL = "level";
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
     * sjekker om en bruker existsInTable i databasen
     * @param con Connection objekt
     * @param id String userID
     * @return returner true om existsInTable, false om ikke
     */
    public static boolean existsInTable(Connection con, String id, String table) {
        // sql spørring for å hente bruker fra tabell
        String userExistsQuery = "SELECT * FROM " + table + " WHERE id = ?;";

        // prøver å kjøre query
        try(PreparedStatement statement = con.prepareStatement(userExistsQuery)) {
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();

            // hvis rs har next, så existsInTable bruker
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

