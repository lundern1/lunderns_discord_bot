package org.example.ListenerMappe;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.Main;

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

            // sjekker om man prøver seg på aram
            if(rp.getDetails().equals(("Howling Abyss (ARAM)")) || rp.getDetails().equals(("Howling Abyss (Custom)"))){


                // sjekker om man går ingame - lov å være i champ select, lobby og queue
                if (rp.getState().equals("In Game")){
                    TextChannel channel;

                    // sjekk om config feltet er gyldig å hente fra
                    try {
                        String msgChannelID = Main.config.get("GUILDRMSGCHANNELID");
                        channel = guild.getTextChannelById(msgChannelID);
                        String id = user.getId();
                        channel.sendMessage("<@"+id+"> ble bannet! grunn: spiller aram :skull:").queue();
                    }
                    catch (Exception e){
                        throw new RuntimeException(e);
                    }

                    // **** kode som banner en bruker fra serveren *****
                    //Member member = event.getMember();
                    //guild.ban(member,7, TimeUnit.DAYS).queue(); OBS! banner en bruker fra serveren
                }
            }
        }
    }
}
