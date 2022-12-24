package org.example.listeners.messagelisteners;

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

/**
 * klasse som lytter etter commands fra eier av discord serveren
 */
public class OwnerListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String userID = event.getAuthor().getId();
        String message = event.getMessage().getContentDisplay();
        TextChannel textChannel = event.getChannel().asTextChannel();

        // prøv å hente eier sin ID fra .env fil
        try {
            String ownerID = Main.config.get("MY_USERID");

            // hvis gyldig bruker utfør switch test
            if (ownerID.equals(userID)) {

                // switch test for å se hvilken command som skal bli utført
                switch (message) {
                    case "!rolesHere":
                        execRolesHere(event);
                        break;
                    case "!delete":
                        execDeleteChannel(textChannel);
                        break;
                    case "!clearChat":
                        execClearChat(textChannel);
                        break;
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * funksjon som sletter en kanal og lager den på nytt
     * @param textChannel hvilken kanal som skal bli slettet
     */
    private void execDeleteChannel(TextChannel textChannel) {
        textChannel.createCopy().queue();
        textChannel.delete().queue();
    }

    /**
     * sletter alle meldinger i en kanal
     * @param textChannel kanal meldinger skal bli slettet i
     */
    private void execClearChat(TextChannel textChannel) {
        MessageHistory history = MessageHistory.getHistoryFromBeginning(textChannel).complete();
        List<Message> mess = history.getRetrievedHistory();
        for(Message m: mess){
            m.delete().queue();
        }
    }

    /**
     * viser en embed-melding av roller man kan få i en kanal
     * @param event eventobjekt av bruker som trigget meldingen
     */
    private void execRolesHere(MessageReceivedEvent event) {
        EmbedRoleMessage embedRoleMessage = new EmbedRoleMessage();
        ArrayList<String> listOfEmojis = embedRoleMessage.getListOfEmojis();
        event.getChannel().sendMessageEmbeds(embedRoleMessage.build()).queue(embedmelding -> {
                    listOfEmojis.forEach(emoji -> {
                        embedmelding.addReaction(Emoji.fromUnicode(emoji)).queue();
                    });
                }
        );
        event.getMessage().delete().queue();
    }
}