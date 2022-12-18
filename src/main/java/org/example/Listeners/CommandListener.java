package org.example.Listeners;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.FileUpload;
import org.example.MyUtilsMappe.MyUtils;

import java.io.File;
import java.util.ArrayList;

import static org.example.MyUtilsMappe.FolderReader.getFiles;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("emma")){
            File[] files = getFiles("Ellingsen");
            int i = MyUtils.getRandomNumber(0, files.length-1);
            event.reply("").addFiles(FileUpload.fromData(files[i])).queue();
        }
        if (command.equals("jenna")){
            File[] files = getFiles("Ortega");
            int i = MyUtils.getRandomNumber(0, files.length-1);
            event.reply("").addFiles(FileUpload.fromData(files[i])).queue();
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {

        ArrayList<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("emma", "få et bilde av emma ellingsen"));
        commandData.add(Commands.slash("jenna", "få et bilde av jenna ortega AKA wednesday"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("command listener klar!");
    }
}
