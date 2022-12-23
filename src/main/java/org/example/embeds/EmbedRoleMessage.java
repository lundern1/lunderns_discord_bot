package org.example.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import org.example.utils.FolderReader;

import java.util.ArrayList;

public class EmbedRoleMessage extends EmbedBuilder {
    ArrayList<String[]> listOfFields;
    ArrayList<String> listOfEmojis;
    public EmbedRoleMessage(){
        super();
        listOfEmojis = new ArrayList<>();

        this.setTitle("Roles Reaction");
        this.setDescription("velg en rolle som passer deg: ");
        this.setColor(0xf76ce5);

        listOfFields = FolderReader.getContentFromFile("rolesreaction.txt");

        listOfFields.forEach(field ->{
            this.addField(field[0], field[1], false);
            String[] firsMsgSplit = field[0].split(" ");
            listOfEmojis.add(firsMsgSplit[0]);
        });
    }

    public ArrayList<String> getListOfEmojis(){
        return listOfEmojis;
    }
}
