package org.example.listeners;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.Main;
import org.example.embeds.EmbedRoleMessage;

import java.util.ArrayList;
import java.util.List;


public class OwnerListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String userID = event.getAuthor().getId();
        String message = event.getMessage().getContentDisplay();
        TextChannel textChannel = event.getChannel().asTextChannel();
        try {
            String ownerID = Main.config.get("MY_USERID");
            if (ownerID.equals(userID)) {
                switch (message) {
                    case "!rolesHere":
                        EmbedRoleMessage embedRoleMessage = new EmbedRoleMessage();
                        ArrayList<String> listOfEmojis = embedRoleMessage.getListOfEmojis();
                        event.getChannel().sendMessageEmbeds(embedRoleMessage.build()).queue(embedmelding -> {
                                    listOfEmojis.forEach(emoji -> {
                                        embedmelding.addReaction(Emoji.fromUnicode(emoji)).queue();
                                    });
                                }
                        );
                        event.getMessage().delete().queue();
                        break;
                    case "!delete":
                        textChannel.createCopy().queue();
                        textChannel.delete().queue();
                        break;
                    case "!clearChat":
                        MessageHistory history = MessageHistory.getHistoryFromBeginning(textChannel).complete();
                        List<Message> mess = history.getRetrievedHistory();
                        for(Message m: mess){
                            m.delete().queue();
                        }
                        break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}