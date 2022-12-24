package org.example.listeners.reactionlisteners;


import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.Main;

/**
 * klasse som lytter etter reaksjoner på å få rolle
 */
public class RoleListener extends ListenerAdapter {

    // lytt hacke-måte å gjøre det på, kan leses fra fil eller hentes på en bedre måte fra discord
    private static final String WEEB_ROLE_ID = "1055495594132652052";
    private static final String HORSE_ROLE_ID = "1055496192680804453";
    private static final String MC_ROLE_ID = "1055496810317217832";

    /**
     * lytter på reaksjon på melding om roller
     * @param event eventobjekt som fyrte reaksjon
     */
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        String emojiPressed = event.getEmoji().asUnicode().getFormatted();

        // switch case om hvilken rolle man vil ha
        switch (emojiPressed){
            case "🍣":
                giveRole(event, WEEB_ROLE_ID);
                break;
            case "🐴":
                giveRole(event, HORSE_ROLE_ID);
                break;
            case "💎":
                giveRole(event, MC_ROLE_ID);
                break;
        }

    }

    /**
     * lytter til om bruker fjerner reaksjon fra melding
     * @param event eventobjekt om reaksjon som trigget event
     */
    public void onMessageReactionRemove(MessageReactionRemoveEvent event){
        String emojiPressed = event.getEmoji().asUnicode().getFormatted();

        // switch case om hvilken rolle som skal bli fjernet
        switch (emojiPressed){
            case "🍣":
                removeRole(event, WEEB_ROLE_ID);
                break;
            case "🐴":
                removeRole(event, HORSE_ROLE_ID);
                break;
            case "💎":
                removeRole(event, MC_ROLE_ID);
                break;
        }
    }

    /**
     * funksjon som fjerner rollen fra bruker
     * @param event eventobjekt som fyrte rollefjerning
     * @param roleID rolle som skal bli fjernet
     */
    private void removeRole(MessageReactionRemoveEvent event, String roleID) {

        // prøver å hente reaction kanal fra .env
        try {
            String reactionGuild = Main.config.get("GUILDREACTION");

            // hvis reaksjon var i gyldig kanal fjern rolle fra bruker som reagerte
            if (event.getChannel().getId().equals(reactionGuild)){
                Role role = event.getGuild().getRoleById(roleID);
                Member member = event.getGuild().getMemberById(event.getUserId());
                event.getGuild().removeRoleFromMember(member, role).queue();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * funksjon som gir rolle til bruker
     * @param event eventobjekt som ble trigget av en reaksjon
     * @param roleID id til rolle som skal bli gitt
     */
    public void giveRole(MessageReactionAddEvent event, String roleID){

        // prøver å hente reaksjon kanal fra .env
        try {
            String reactionGuild = Main.config.get("GUILDREACTION");

            // hvis kanal er gyldig gi rolle til bruker som reagerte
            if (event.getChannel().getId().equals(reactionGuild)){
                Role role = event.getGuild().getRoleById(roleID);
                Member member = event.getGuild().getMemberById(event.getUserId());
                event.getGuild().addRoleToMember(member, role).queue();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
