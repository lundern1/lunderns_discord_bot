package org.example.Listeners;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.RichPresence;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.Main;

/**
 * klasse som lytter til aktiviteten til en bruker som hører på
 * musikk via Spotify
 */
public class SpotifyListener extends ListenerAdapter {
    private String lastSongPlayed = "";

    /**
     * gir beskjed om at listeneren har lastet globalt
     * @param event
     */
    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("spotify listener klar!");
    }

    /**
     * lytter etter om man hører på ny sang på spotify for hver ny aktivitet
     * @param event eventobjekt som fyrte en ny sang
     */
    @Override
    public void onUserActivityStart(UserActivityStartEvent event) {
        Activity activity = event.getNewActivity();
        RichPresence rp = activity.asRichPresence();

        // hvis man hører på spotify og forrige sang man hørte på ikke var lik som denne
        if (rp.getName().equals("Spotify") && !lastSongPlayed.equals(rp.getDetails()))
            sendSpotifySong(event, rp);
    }

    /**
     * sender spotify sang som blir hørt på i discord
     * @param event eventobjekt som fyrte på en ny sang
     * @param rp richpresence av eventet
     */
    public void sendSpotifySong(UserActivityStartEvent event, RichPresence rp){
        Guild guild = event.getGuild();
        User user = event.getUser();

        lastSongPlayed = rp.getDetails();
        String newSongAsString = user.getAsTag()+" hører på: " + rp.getState().split(";")[0] + " - " + rp.getDetails();

        // prøv å se om kanal id til spotify fungerte.
        try {
            String generalID = Main.config.get("GUILDSPOTIFY");
            TextChannel channel = guild.getTextChannelById(generalID);
            channel.sendMessage(newSongAsString).queue();
        }
        catch (Exception e){
            throw new RuntimeException();
        }
    }
}
