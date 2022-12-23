package org.example.database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

/**
 * klasse for å håndtere database-spørringer og connections
 */
public class ConnectionHandler {

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
     * lukker connection til poolet
     */
    private static void closeDatasource() {
        dataSource.close();
    }



}

