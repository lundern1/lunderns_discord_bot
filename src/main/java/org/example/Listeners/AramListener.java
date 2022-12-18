package org.example.Listeners;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.concurrent.TimeUnit;

public class AramListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("aram listener klar!");
    }

    @Override
    public void onUserActivityStart(UserActivityStartEvent event){
        User user = event.getUser();
        Activity activity = event.getNewActivity();
        Guild guild = event.getGuild();
        RichPresence rp = activity.asRichPresence();

        if (rp.getDetails() != null){
            // sjekker om man prøver seg på aram
            if(rp.getDetails().equals(("Howling Abyss (ARAM)")) || rp.getDetails().equals(("Howling Abyss (Custom)"))){

                // sjekker om man går ingame - lov å være i champ select, lobby og queue
                if (rp.getState().equals("In Game")){
                    TextChannel channel = guild.getTextChannelsByName("general", true).get(0);
                    String id = user.getId();
                    channel.sendMessage("<@"+id+"> ble bannet! grunn: spiller aram :skull:").queue();


                    Member member = event.getMember();
                    //guild.ban(member,7, TimeUnit.DAYS).queue(); OBS! banner en bruker fra serveren
                }
            }
        }

    }


}
