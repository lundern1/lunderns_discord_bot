package org.example.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.database.DatabaseHandler;

/**
 * klasse som lar brukeren spille spill
 * meldinger må starte med '!'
 */
public class AccountListener extends ListenerAdapter {

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
        String message = event.getMessage().getContentDisplay();
        String userID = event.getAuthor().getId();
        switch (message){
            case "!nybruker":
                try{
                    boolean suksess = DatabaseHandler.skrivTilDB(userID, 500);
                    if (suksess)
                        event.getChannel().sendMessage("du er registrert!").queue();
                    else {
                        event.getChannel().sendMessage("noe gikk galt").queue();
                    }
                } catch (Exception e){
                    event.getChannel().sendMessage("en feil has skjedd: " + e.getMessage()).queue();
                }
                break;
            case "!balance":
                try {
                    int balance = DatabaseHandler.getBalance(userID);
                    event.getChannel().sendMessage(balance + "").queue();
                } catch (Exception e) {
                    event.getChannel().sendMessage("en feil has skjedd: " + e.getMessage()).queue();
                }
                break;
            case "!delete":
                try {
                    boolean sukess = DatabaseHandler.deleteBruker(userID);
                    if (sukess)
                        event.getChannel().sendMessage("bruker slettet").queue();
                    else
                        event.getChannel().sendMessage("noe gikk galt").queue();
                } catch (Exception e) {
                    event.getChannel().sendMessage("en feil has skjedd: " + e.getMessage()).queue();
                }
                break;
            case "!pray4lundern": case "!p4l":
                try {
                    boolean suksess = DatabaseHandler.updateBalance(userID, DatabaseHandler.getBalance(userID)+10);
                    if (suksess)
                        event.getChannel().sendMessage("lundern har hørt din bønn, her er 10 flus").queue();
                    else
                        event.getChannel().sendMessage("noe gikk galt");
                }
                catch (Exception e) {
                    event.getChannel().sendMessage("en feil has skjedd: " + e.getMessage()).queue();
                }
        }

    }


}
