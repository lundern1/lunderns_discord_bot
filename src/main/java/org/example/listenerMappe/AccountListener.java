package org.example.listenerMappe;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.spillmappe.Coinflip;
import org.example.database.DatabaseHandler;

import static org.example.myUtilsMappe.MyUtils.getSumFromString;

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
            case "!pray4lundern": case "!p4l":
                if (DatabaseHandler.updateBalance(userID, DatabaseHandler.getBalance(userID)+10))
                    event.getChannel().sendMessage("lundern har hørt din bønn, her er 10 flus").queue();
                else
                    event.getChannel().sendMessage("feilmelding");
        }

    }


}
