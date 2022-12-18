package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import org.example.Listeners.AramListener;
import org.example.Listeners.MessageListener;

import static org.example.MyUtilsMappe.JDASettings.addEvents;
import static org.example.MyUtilsMappe.JDASettings.createJDA;

public class Main {
    public static final Dotenv config = Dotenv.configure().ignoreIfMissing().load();;
    public static void main(String[] args) {
        String token = config.get("TOKEN");

        JDA jda = createJDA(token);
        addEvents(jda);

    }
}
