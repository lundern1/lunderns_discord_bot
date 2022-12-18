package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import org.example.MyUtilsMappe.FolderReader;

import java.io.File;

import static org.example.MyUtilsMappe.FolderReader.getFiles;
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
