package org.example.listeners.messagelisteners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.Main;
import org.example.database.ConnectionHandler;
import org.example.database.LevelHandler;
import org.example.embeds.EmbedHelpMessage;
import org.example.embeds.EmbedMessagesMessage;
import org.example.embeds.EmbedRoleMessage;
import org.example.utils.MyUtils;

public class LevelListener extends ListenerAdapter {
    private static final int XP = 10;
    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("level listener klar!");
    }


    /**
     * funksjon som lytter på meldinger
     * @param event eventobjekt som ble fyrt av sendt melding
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (MyUtils.ifBotOrNotFromGuild(event))
            return;


        // henter melding og brukerID til bruker som sendte melding
        String message = event.getMessage().getContentDisplay();
        String userID = event.getAuthor().getId();

        // hvis bruker ikke er botten kjør level-updates
        if(!event.getAuthor().isBot()){
            updateMessages(userID, event);
            updateXp(userID);
            lookForLevelUp(event, userID);
        }

        // switch for ulike mulige commands
        switch (message){
            case "!rank":
                getRow(userID, event, "rank");
                break;
            case "!xp":
                getRow(userID, event, "xp");
                break;
            case "!messages":
                getRow(userID, event, "messages");
                break;
            case "!top":
                EmbedMessagesMessage embedMessagesMessage = new EmbedMessagesMessage(event);
                event.getChannel().sendMessageEmbeds(embedMessagesMessage.build()).queue();
                break;
        }
    }

    /**
     * sjekker om brukeren kvalifiserer til nytt level
     * @param event event trigget fra melding
     * @param userID bruker som trigget melding
     */
    private void lookForLevelUp(MessageReceivedEvent event, String userID) {
        int currentXp = LevelHandler.getRowFromLevel(userID, "xp");

        // er xp over 250 så gå opp 1 i level
        if (currentXp >= 250){

            // oppdateringer i DB
            int newRank = LevelHandler.getRowFromLevel(userID, "rank")+1;
            LevelHandler.updateRow(userID, newRank, "rank");
            LevelHandler.updateRow(userID, 0, "xp");
            String id = event.getAuthor().getAsMention();

            // send melding i kanal om nytt level
            event.getChannel().sendMessage(id+" har nå gått opp i rank og er nå level: " + newRank).queue();
        }
    }

    /**
     * oppdaterer xpen til en bruker for hver melding
     * @param userID bruker som skal oppdateres
     */
    private void updateXp(String userID) {
        int newXp = LevelHandler.getRowFromLevel(userID, "xp") + XP;
        LevelHandler.updateRow(userID, newXp, "xp");
    }

    /**
     * oppdaterer antall meldinger en bruker har sendt
     * @param userID bruker som skal oppdateres
     * @param event event trigget av bruker
     */
    private void updateMessages(String userID, MessageReceivedEvent event) {

        // prøver å oppdatere bruker sine meldinger i db
        try{
            boolean suksess = LevelHandler.updateRow(userID, LevelHandler.getRowFromLevel(userID, "messages")+1, "messages");

            // feilmelding om ikke oppdatert
            if (!suksess)
                System.out.println("noe feil med oppdatering av meldinger");
        } catch (Exception e) {
            event.getChannel().sendMessage("en feil has skjedd: " + e.getMessage()).queue();
        }
    }

    /**
     * henter en rad fra database og sender verdi som melding
     * @param userID bruker som skal hentes fra
     * @param event event trigget av bruker
     * @param row rad som skal hentes
     */
    private void getRow(String userID, MessageReceivedEvent event, String row) {
        // prøver å hente rad til bruker fra database
        try {
            int valueFromRow = LevelHandler.getRowFromLevel(userID, row);

            // hvis verdi ikke hentet send feilmelding
            if (valueFromRow == -1)
                event.getChannel().sendMessage("noe har gått galt");
            // else send verdi som melding
            else
                event.getChannel().sendMessage(row + ": " + valueFromRow).queue();
        } catch (Exception e) {
            event.getChannel().sendMessage("en feil has skjedd: " + e.getMessage()).queue();
        }
    }
}
