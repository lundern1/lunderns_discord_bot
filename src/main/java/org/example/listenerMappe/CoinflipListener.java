package org.example.listenerMappe;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.database.DatabaseHandler;
import org.example.spillmappe.Coinflip;

import static org.example.myUtilsMappe.MyUtils.getSumFromString;

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
        if (message.contains("!coinflip ")){
            int betAmount = getSumFromString(message);
            String tilbakemelding = "ugyldig sum";
            if (betAmount != -1) {
                Coinflip coinflip = new Coinflip(userID, betAmount);
                tilbakemelding = coinflip.coinflipGame();
            }
            tilbakemelding += "\ndin sum: " + DatabaseHandler.getBalance(userID);
            event.getChannel().sendMessage(tilbakemelding).queue();
        }
    }
}
