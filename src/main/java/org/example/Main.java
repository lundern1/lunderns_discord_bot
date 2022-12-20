package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import org.example.spillmappe.DatabaseHandler;

import static org.example.MyUtilsMappe.JDASettings.addEvents;
import static org.example.MyUtilsMappe.JDASettings.createJDA;

/******************************************
 * Discord bot som blant annet:           *
 * - banner folk som spiller aram         *
 * - logger spotify sanger man hører på   *
 * - lytter på meldinger folk sender      *
 * - slash command for å få bilder        *
 * - '!' gambling spill                   *
 * @lundern1 - github                     *
 ******************************************/
public class Main {
    public static final Dotenv config = Dotenv.configure().ignoreIfMissing().load();;
    public static void main(String[] args) {
        String token = config.get("TOKEN");
        DatabaseHandler.initConnection();

        JDA jda = createJDA(token);
        addEvents(jda);

    }
}
