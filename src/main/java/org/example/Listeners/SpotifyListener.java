package org.example.Listeners;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.RichPresence;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SpotifyListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("spotify listener klar!");
    }

    private String lastSong = "";

    @Override
    public void onUserActivityStart(UserActivityStartEvent event) {
        User user = event.getUser();
        Activity activity = event.getNewActivity();
        Guild guild = event.getGuild();
        RichPresence rp = activity.asRichPresence();

        if (rp.getName().equals("Spotify") && !lastSong.equals(rp.getDetails())){
            lastSong = rp.getDetails();
            event.getJDA().getTextChannelById("1054105565141405736").sendMessage(user.getAsTag()+" hører på: " + rp.getState().split(";")[0] + " - " + rp.getDetails()).queue();
        }
    }
}
