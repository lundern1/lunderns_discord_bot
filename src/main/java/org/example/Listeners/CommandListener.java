package org.example.Listeners;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.ArrayList;

public class CommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("emma")){
            event.reply("kommer snart!").queue();
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {

        ArrayList<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("emma", "få et bilde av emma ellingsen"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("command listener klar!");
    }
}
