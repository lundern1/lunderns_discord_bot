package org.example.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.example.database.LevelHandler;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * klasse som lager en embed melding for å vise hvem som har sendt flest meldinger
 * på serveren.
 * extender EmbedBuilder
 */
public class EmbedMessagesMessage extends EmbedBuilder {
    private ArrayList<String[]> listOfUsersMessage;


    /**
     * konstruktør som lager embed meldingen
     * @param event eventobjekt som ble trigget av
     *              en bruker som sendte melding
     */
    public EmbedMessagesMessage(MessageReceivedEvent event){
        listOfUsersMessage = LevelHandler.getRowForAllUsers(event, "messages");

        this.setTitle("eliten av serveren");
        this.setDescription("hvem er mest aktiv: ");
        this.setColor(0xf76ce5);

        // bruker en atomic integer for å automatisk inkrementere hvilken rank man har
        AtomicInteger rankCounter = new AtomicInteger(1);

        // looper gjennom antall brukere som har blitt hentet fra databasen
        listOfUsersMessage.forEach(field ->{
            String userName = "";

            // switch som setter medaljer på topp 3
            switch (rankCounter.get()){
                case 1:
                    userName += "🥇";
                    rankCounter.getAndIncrement();
                    break;
                case 2:
                    userName += "🥈";
                    rankCounter.getAndIncrement();
                    break;
                case 3:
                    userName += "🥉";
                    rankCounter.getAndIncrement();
                    break;
                default:
                    userName += rankCounter.getAndIncrement();
                    break;
            }
            userName += ". " +field[0] + ": ";
            String numberOfMessages = field[1] + " meldinger sendt!";

            // legger bruker i embed melding
            this.addField(userName, numberOfMessages, false);
        });
    }
}
