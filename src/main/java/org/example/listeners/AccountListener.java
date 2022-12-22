package org.example.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.Main;
import org.example.database.DatabaseHandler;
import org.example.utils.MyUtils;


/**
 * klasse som lar brukeren spille spill
 * meldinger må starte med '!'
 */
public class AccountListener extends ListenerAdapter {
    private static final int GIVE_MONEY = 10;

    /**
     *  gir beskjed om at listeneren har lastet globalt
     * @param event
     */
        @Override
        public void onReady(ReadyEvent event) {
            System.out.println("game listener klar!");
        }

    /**
     * lytter til meldinger fra bruker
     * @param event event objekt av bruker som sendte melding
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // henter melding og brukerID til bruker som sendte melding
        String message = event.getMessage().getContentDisplay();
        String userID = event.getAuthor().getId();


        // sjekker ulike typer kommando om samsvar med melding sendt
        switch (message){
            // bruker vil sjekke kontoen sin
            case "!balance":
                execBalanceCommand(userID, event);
                break;
            // øker brukerens balance med 10
            case "!pray4lundern": case "!p4l":
                execPrayCommand(userID, event, GIVE_MONEY);
                break;
            case "!julebonus":
                execPrayCommand(userID, event, 1000);
                break;
        }

    }

    private void execBalanceCommand(String userID, MessageReceivedEvent event) {
        // prøver å hente balance til bruker fra database
        try {
            int balance = DatabaseHandler.getBalance(userID);
            event.getChannel().sendMessage(balance + "").queue();
        } catch (Exception e) {
            event.getChannel().sendMessage("en feil has skjedd: " + e.getMessage()).queue();
        }
    }
    private void execPrayCommand(String userID, MessageReceivedEvent event, int addToBalance){
        // prøver å øke balance til bruker med x antall sum
        try {
            boolean suksess = DatabaseHandler.updateBalance(userID, DatabaseHandler.getBalance(userID)+addToBalance);
            // gir tilbakemelding på om balance ble økt eller ikke
            if (suksess)
                event.getChannel().sendMessage("lundern har hørt din bønn, her er "+addToBalance+" flus").queue();
            else
                event.getChannel().sendMessage("noe gikk galt").queue();
        }
        catch (Exception e) {
            event.getChannel().sendMessage("en feil has skjedd: " + e.getMessage()).queue();
        }
    }


}


