package org.example.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import org.example.utils.FolderReader;

import java.util.ArrayList;

/**
 * klasse for å lage embed-melding fra !rolesHere command
 */
public class EmbedRoleMessage extends EmbedBuilder {
    ArrayList<String[]> listOfFields;
    ArrayList<String> listOfEmojis;

    /**
     * konstruktør for å lage embed-melding
     */
    public EmbedRoleMessage(){
        super();
        listOfEmojis = new ArrayList<>();

        this.setTitle("Roles Reaction");
        this.setDescription("velg en rolle som passer deg: ");
        this.setColor(0xf76ce5);

        listOfFields = FolderReader.getContentFromFile("rolesreaction.txt");

        // looper gjennom antall roller hentet fra fil
        listOfFields.forEach(field ->{
            this.addField(field[0], field[1], false);
            String[] firsMsgSplit = field[0].split(" ");
            listOfEmojis.add(firsMsgSplit[0]);
        });
    }

    /**
     * funksjon som henter listen av emojier som representerer en rolle
     * @return Arraylist av emojier som String
     */
    public ArrayList<String> getListOfEmojis(){
        return listOfEmojis;
    }
}
