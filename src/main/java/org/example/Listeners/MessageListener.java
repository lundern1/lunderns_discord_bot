package org.example.Listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.Main;

import static org.example.MyUtilsMappe.MyUtils.getRandomNumberInRange;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("message listener klar!");
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] listeNormal = {"nei", "ja", "stor sjanse", "det vil tiden vise", "sannsynligvis"};
        String[] listeTroll = {"nei", "haha aldri", "ikke for deg"};

        String message = event.getMessage().getContentDisplay();

        if (message.equals("liker du rimming")) {
            event.getChannel().sendMessage(":+1:").queue();
        }

        if (message.equals("test")){
            String myUserid = Main.config.get("MY_USERID");
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
}
