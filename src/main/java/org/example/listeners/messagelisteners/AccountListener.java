package org.example.listeners.messagelisteners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.database.ConnectionHandler;
import org.example.database.UserHandler;


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

    /**
     * sender en melding på discord om bruker sin balance
     * @param userID bruker som spurte om balance
     * @param event eventobjekt av melding sendt
     */
    private void execBalanceCommand(String userID, MessageReceivedEvent event) {
        // prøver å hente balance til bruker fra database
        try {
            int balance = UserHandler.getBalance(userID);
            event.getChannel().sendMessage(balance + "").queue();
        } catch (Exception e) {
            event.getChannel().sendMessage("en feil has skjedd: " + e.getMessage()).queue();
        }
    }

    /**
     * funksjon som gir bruker litt mer balance
     * @param userID bruker som vil ha mer balance
     * @param event eventobjekt av melding som ble sendt
     * @param addToBalance int av hvor mye som skal bli økt i balance
     */
    private void execPrayCommand(String userID, MessageReceivedEvent event, int addToBalance){
        // prøver å øke balance til bruker med x antall sum
        try {
            boolean suksess = UserHandler.updateBalance(userID, UserHandler.getBalance(userID)+addToBalance);
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


