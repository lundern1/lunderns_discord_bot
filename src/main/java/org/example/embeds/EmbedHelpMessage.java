package org.example.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import org.example.utils.FolderReader;

import java.util.ArrayList;

/**
 * klasse som bygger !help melding med forklaring for hver kommando
 */
public class EmbedHelpMessage extends EmbedBuilder {

    /**
     * konstuktør som bygger meldingen
     */
    public EmbedHelpMessage(){
            super();
            // informasjon om meg som lagde botten(kunne vært const eller hentet fra fil)
            String githubUrl = "https://github.com/lunderns_discord_bot";
            String authorUrl = "https://github.com/lundern1";
            String authorImageUrl = "https://avatars.githubusercontent.com/u/101733777?s=400&u=9b4575974e42b4d93999ad7f215ce0aef9082474&v=4";

            this.setTitle("lunderns discord bot!", githubUrl);
            this.setDescription("her får du hjelp til ulike commands");
            this.setColor(0xf76ce5);

            ArrayList<String[]> listOfFields = FolderReader.getContentFromFile("messagecommands.txt");

            // looper gjennom antall commands hentet fra fil
            listOfFields.forEach(field -> this.addField(field[0], field[1], false));

            this.setAuthor("lundern", authorUrl, authorImageUrl);
        }
}
