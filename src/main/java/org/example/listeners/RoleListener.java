package org.example.listeners;


import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.Main;


public class RoleListener extends ListenerAdapter {
    private static final String WEEB_ROLE_ID = "1055495594132652052";
    private static final String HORSE_ROLE_ID = "1055496192680804453";
    private static final String MC_ROLE_ID = "1055496810317217832";

    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        String emojiPressed = event.getEmoji().asUnicode().getFormatted();
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

    public void onMessageReactionRemove(MessageReactionRemoveEvent event){
        String emojiPressed = event.getEmoji().asUnicode().getFormatted();
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

    private void removeRole(MessageReactionRemoveEvent event, String roleID) {
        try {
            String reactionGuild = Main.config.get("GUILDREACTION");
            if (event.getChannel().getId().equals(reactionGuild)){
                Role role = event.getGuild().getRoleById(roleID);
                Member member = event.getGuild().getMemberById(event.getUserId());
                event.getGuild().removeRoleFromMember(member, role).queue();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void giveRole(MessageReactionAddEvent event, String roleID){
        try {
            String reactionGuild = Main.config.get("GUILDREACTION");
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
