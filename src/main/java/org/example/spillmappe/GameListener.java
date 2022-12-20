package org.example.spillmappe;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * klasse som lar brukeren spille spill
 * meldinger må starte med '!'
 */
public class GameListener extends ListenerAdapter {

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
                boolean bool = DatabaseHandler.skrivTilDB(userID, 500);
                if (bool)
                    event.getChannel().sendMessage("du er registrert!").queue();
                else {
                    event.getChannel().sendMessage("noe gikk galt").queue();
                }
                break;
            case "!balance":
                int balance = DatabaseHandler.getBalance(userID);
                event.getChannel().sendMessage(balance + "").queue();
                break;
            case "!delete":
                if (DatabaseHandler.deleteBruker(userID))
                    event.getChannel().sendMessage("bruker slettet").queue();
                else
                    event.getChannel().sendMessage("feilmelding").queue();
                break;
        }

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

    /**
     * funksjon som sjekker om bruker har gitt
     * gyldig beløp å gamble med
     * @param message String melding fra bruker
     * @return int returnerer beløp bruker vil spille for, -1 om ugyldig beløp
     */
    private int getSumFromString(String message) {
        String[] liste = message.split(" ");
        try{
            return Integer.parseInt(liste[1]);
        } catch (NumberFormatException e) {
            return -1;
        } catch (ArrayIndexOutOfBoundsException e){
            return -1;
        }
    }
}
