package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class MyEventListener extends ListenerAdapter {
    private static Dotenv config;

    @Override
    public void onReady(ReadyEvent event)
    {
        config = Dotenv.configure().ignoreIfMissing().load();
        System.out.println("I am ready to go!");
    }

    @Override
    public void onUserActivityStart(UserActivityStartEvent event){
        User user = event.getUser();
        Activity activity = event.getNewActivity();
        Guild guild = event.getGuild();
        RichPresence rp = activity.asRichPresence();

        System.out.println(rp.getDetails());

        if (rp.getDetails() != null){
            // sjekker om man prøver seg på aram
            if(rp.getDetails().equals(("Howling Abyss (ARAM)")) || rp.getDetails().equals(("Howling Abyss (Custom)"))){

                // sjekker om man går ingame - lov å være i champ select, lobby og queue
                if (rp.getState().equals("In Game")){
                    TextChannel channel = guild.getTextChannelsByName("general", true).get(0);
                    String id = user.getId();
                    channel.sendMessage("<@"+id+"> ble bannet! grunn: spiller aram :skull:").queue();


                    Member member = event.getMember();
                    guild.ban(member,7, TimeUnit.DAYS).queue();
                }
            }
        }

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] listeNormal = {"nei", "ja", "stor sjanse", "det vil tiden vise", "sannsynligvis"};
        String[] listeTroll = {"nei", "haha aldri", "ikke for deg"};

        String message = event.getMessage().getContentDisplay();

        if (message.equals("liker du rimming")) {
            event.getChannel().sendMessage(":+1:").queue();
        }

        if (message.equals("blir det fut champs på meg?")){
            String myUserid = config.get("MY_USERID");
            if (event.getMember().getUser().getId().equals(myUserid)){
                int i = getRandomNumberInRange(0, listeNormal.length-1);
                event.getChannel().sendMessage(listeNormal[i]).queue();
            }
            else {
                int i = getRandomNumberInRange(0, listeTroll.length-1);
                event.getChannel().sendMessage(listeTroll[i]).queue();
            }
        }
        if (message.equals("takk gud")){
            event.getChannel().sendMessage("takk meg").queue();
        }
    }
    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
