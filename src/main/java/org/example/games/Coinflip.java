package org.example.games;

import org.example.database.DatabaseHandler;
import org.example.utils.MyUtils;

import java.sql.Connection;
import java.sql.SQLException;

import static org.example.database.DatabaseHandler.*;

/**
 * klasse som representerer et spill med coinflip
 */
public class Coinflip {
    private static final int chanceToWinPercent = 50;
    private String userID;
    private int betAmount;

    public Coinflip(String userID, int betAmount){
        this.userID = userID;
        this.betAmount = betAmount;

    }

    /**
     * hånterer et spill med coinflip
     * @return returnerer tilbakemelding på om du vant,
     * det er ugyldig sum eller om brukeren ikke existsInTable
     */
    public String coinflipGame() {
        // sjekker om summen er gyldig
        if (betAmount < 1) return "ugyldig sum";

        // prøver å få connection med database
        try (Connection connection = DatabaseHandler.dataSource.getConnection()) {

            // sjekker om bruker eksisterer
            if (!existsInTable(connection, userID, TABLE_USER))
                DatabaseHandler.skrivBrukerTilUser(userID, 500);


            // henter bruker sin balance
            int currentBalance = getBalance(userID);

            // sjekker om bruker han nok til å gjennomføre bet
            if (betAmount > currentBalance)
                return "ikke nok penger";

            // henter resultat fra coinflip
            boolean userWon = getCoinResult();

            // oppdaterer balance til bruker etter tap eller vinn
            if (userWon) {
                updateBalance(userID, currentBalance + betAmount);
                return "du vant! ";
            } else {
                updateBalance(userID, currentBalance - betAmount);
                return "du tapte! ";
            }
        } catch (SQLException e) {
            System.out.println("en feil med database: " + e.getMessage());
            return "en databasefeil skjedde";
        }

    }

    /**
     * gir et resultat av en coinflip avhengig av sjanse til å vinne
     * @return returnerer boolean om man vant eller ikke
     */
    private static boolean getCoinResult() {
        // generer tilfeldig nummer mellom 1-100
        int random = MyUtils.getRandomNumber(1, 100);

        // hvis nummer er høyere enn sjanse til å vinne returner tap
        if (random > chanceToWinPercent)
            return false;
        // ellers returner seier
        return true;
    }
}
