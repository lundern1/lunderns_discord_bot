package org.example.listeners.activitylisteners;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.Main;
import org.example.utils.MyUtils;

import java.util.concurrent.TimeUnit;

/**
 * klasse som lytter på bruker aktivitet
 * i hovedsak etter spilling av league of legends
 */
public class AramListener extends ListenerAdapter {

    /**
     *  gir beskjed om at listeneren har lastet globalt
     * @param event
     */
    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("aram listener klar!");
    }

    /**
     * lytter til en ny aktivitet til en bruker
     * @param event brukeren med ny aktivitet
     */
    @Override
    public void onUserActivityStart(UserActivityStartEvent event){
        if (MyUtils.ifBotOrNotFromGuild(event))
            return;

        Activity activity = event.getNewActivity();
        RichPresence rp = activity.asRichPresence();

        // try aktivitet ikke er null sjekk om bruker spiller aram
        try {
            sjekkForAram(rp, event);
        } catch (NullPointerException e) {
        }

    }

    /**
     * sjekker om brukeren spiller aram.
     * @param rp richpresence av eventet som blir fyrt
     * @param event eventobjekt av eventet som blir fyrt
     */
    public void sjekkForAram(RichPresence rp, UserActivityStartEvent event){
        Guild guild = event.getGuild();
        User user = event.getUser();

        // er spillet LOL?
        if (rp.getName().equals("League of Legends")){

            // sjekker bruker spiller aram eller custom aram game
            if(rp.getDetails().equals(("Howling Abyss (ARAM)")) || rp.getDetails().equals(("Howling Abyss (Custom)"))){

                // sjekker om man går ingame - lov å være i champ select, lobby og queue
                if (rp.getState().equals("In Game")){

                    // sjekk om config feltet er gyldig å hente fra
                    try {
                        // få melding fra config og få text channel fra guild
                        String msgChannelID = Main.config.get("GUILDRMSGCHANNELID");
                        TextChannel channel = guild.getTextChannelById(msgChannelID);

                        // send melding i kanal
                        String id = user.getId();
                        channel.sendMessage("<@"+id+"> ble bannet! grunn: spiller aram :skull:").queue();
                    }
                    catch (NullPointerException e) {
                        System.out.println("Config felt ikke funnet : " + e.getMessage());
                    }
                    catch (IllegalArgumentException e) {
                        System.out.println("invalid text channel ID: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("en feil har skjedd: " + e.getMessage());
                    }
                    // **** kode som banner en bruker fra serveren *****
                    // Member member = event.getMember();
                    // guild.ban(member,7, TimeUnit.DAYS).queue();

                }
            }
        }
    }
}
