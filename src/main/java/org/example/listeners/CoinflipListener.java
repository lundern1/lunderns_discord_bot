package org.example.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.database.DatabaseHandler;
import org.example.games.Coinflip;

import java.util.stream.Stream;

import static org.example.utils.MyUtils.getSumFromString;

/**
 * klasse som lytter etter om bruker vil conflippe
 */
public class CoinflipListener extends ListenerAdapter {

    /**
     * funkson som lytter til meldinger sendt
     * @param event eventobjekt fra melding mottatt
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        String userID = event.getAuthor().getId();

        // sjekker om bruker vil conflippe
        if (message.startsWith("!coinflip ")){
            int betAmount = getSumFromString(message);
            String tilbakemelding = "ugyldig sum";

            if (betAmount == -2){
                int totalSum = DatabaseHandler.getBalance(userID);
                tilbakemelding = execFlip(userID, totalSum);
            }
            else if (betAmount != -1) {
                tilbakemelding = execFlip(userID, betAmount);
            }
            tilbakemelding += "\ndin sum: " + DatabaseHandler.getBalance(userID);
            event.getChannel().sendMessage(tilbakemelding).queue();
        }

    }

    private String execFlip(String userID, int betAmount) {
        String returnMessage = "";
        Coinflip coinflip = new Coinflip(userID, betAmount);
        try{
            returnMessage = coinflip.coinflipGame();
        } catch (Exception e) {
            returnMessage = "en feil har skjedd: " + e.getMessage();
        }
        return returnMessage;
    }
}
