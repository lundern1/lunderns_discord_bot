package org.example.ListenerMappe;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;

import org.example.MyUtilsMappe.MyUtils;

import java.io.File;

import static org.example.MyUtilsMappe.FolderReader.getCommandsFromFile;
import static org.example.MyUtilsMappe.FolderReader.getImageFiles;

/**
 * klasse som lytter på /slash commands
 */
public class CommandListener extends ListenerAdapter {

    /**
     * når en slash command blir fyrt
     * @param event eventet som blir fyrt
     */
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        // switch - hvilken slash blir fyrt
        switch (command.toLowerCase()){
            case "emma":
                replyWithFile(event, "Ellingsen");
                break;
            case "jenna":
                replyWithFile(event, "Ortega");
                break;
            default:
                break;
        }
    }

    /**
     * henter en liste av files
     * velger ut en tilfeldig file(bilde)
     * replyer på discord med file som svar
     * @param event eventet som blir fyrt
     * @param filename foldernavnet der filene ligger
     */
    public void replyWithFile(SlashCommandInteractionEvent event, String filename){
        File[] files = getImageFiles(filename);
        int i = MyUtils.getRandomNumber(0, files.length-1);
        event.reply("").addFiles(FileUpload.fromData(files[i])).queue();
    }

    /**
     * gir beskjed om at listeneren har lastet på guilden
     * @param event
     */
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        event.getGuild().updateCommands().addCommands(getCommandsFromFile()).queue();
    }

    /**
     * gir beskjed om at listeneren har lastet globalt
     * @param event
     */
    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("command listener klar!");
    }
}
