package org.example.myUtilsMappe;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.example.listenerMappe.*;

/**
 * en klasse med alle funksjoner som omhandler JDA
 */
public class JDASettings {

        // bygger en JDA med alle innstillinger man trenger
        public static JDA createJDA(String token){
             return JDABuilder
                    .createDefault(token) // token for discord bot i devolper portal
                    .setActivity(Activity.watching("Wednesday 😍")) // setter aktivitet
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT) // lese meldinger
                    .enableIntents(GatewayIntent.GUILD_PRESENCES) // lese brukers aktivitet
                    .enableIntents(GatewayIntent.GUILD_MEMBERS) // tilgang til medlemmer
                    .enableCache(CacheFlag.ACTIVITY) // tillater caching av bruker aktivitet
                     // de to kommandoene under er tungt for botten unngå på stor server
                    .setMemberCachePolicy(MemberCachePolicy.ALL)  // cacher alle brukere av botten
                    .setChunkingFilter(ChunkingFilter.ALL) // tvinger botten til å cache alle brukere på en gang(unngår lazing caching)
                    .build();
        }

    /**
     * legger til alle listeners man trenger i JDAet
     * @param jda jda man skal legge til i
     */
    public static void addEvents(JDA jda){
            jda.addEventListener(
                    new AramListener(),
                    new CommandListener(),
                    new MessageListener(),
                    new SpotifyListener(),
                    new AccountListener(),
                    new CoinflipListener()
            );
        }
}
