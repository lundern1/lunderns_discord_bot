package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import org.example.database.DatabaseHandler;

import static org.example.utils.JDASettings.addEvents;
import static org.example.utils.JDASettings.createJDA;

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
    public static final Dotenv config = Dotenv.configure().ignoreIfMissing().load();
    public static void main(String[] args) {
        // henter discord-bot-token fra .env fil
        String token = config.get("TOKEN");

        // starter opp database connection pool
        DatabaseHandler.initConnection();

        // starter JDA
        JDA jda = createJDA(token);

        // legger til eventListeners i JDA
        addEvents(jda);

    }
}
