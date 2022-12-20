package org.example.spillmappe;

import org.example.MyUtilsMappe.MyUtils;

import java.sql.Connection;
import java.sql.SQLException;

import static org.example.spillmappe.DatabaseHandler.*;

public class Coinflip {
    private static final int chanceToWinPercent = 50;
    private String userID;
    private int betAmount;

    public Coinflip(String userID, int betAmount){
        this.userID = userID;
        this.betAmount = betAmount;

    }
    public String coinflipGame() {
        if (betAmount < 1) return "ugyldig sum";

        try (Connection connection = dataSource.getConnection()) {
            if (!finnes(connection, userID))
                return "bruker finnes ikke";

            boolean userWon = getCoinResult();
            int currentBalance = getBalance(userID);

            if (betAmount <= currentBalance) {
                if (userWon) {
                    updateBalance(userID, currentBalance + betAmount);
                    return "du vant! ";
                } else {
                    updateBalance(userID, currentBalance - betAmount);
                    return "du tapte! ";
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
    private static boolean getCoinResult() {
        int random = MyUtils.getRandomNumber(1, 100);
        if (random > chanceToWinPercent)
            return false;
        return true;
    }
}
