package org.example.MyUtilsMappe;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.example.Listeners.AramListener;
import org.example.Listeners.CommandListener;
import org.example.Listeners.MessageListener;

import java.util.LinkedList;

public class JDASettings {

        public static JDA createJDA(String token){
             return JDABuilder
                    .createDefault(token) // token for discord bot i devolper portal
                    .setActivity(Activity.watching("Wednesday 😍")) // setter aktivitet
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT) // lese meldinger
                    .enableIntents(GatewayIntent.GUILD_PRESENCES) // lese brukers aktivitet
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .enableCache(CacheFlag.ACTIVITY) // tillater caching av bruker aktivitet

                    // kode fungerer uten disse to - kan bli problemer på større servere
                    //.setMemberCachePolicy(MemberCachePolicy.ALL)  // cacher alle brukere av botten
                    //.setChunkingFilter(ChunkingFilter.ALL) // tvinger botten til å cache alle brukere på en gang(unngår lazing caching)
                    .build();
        }

        public static void addEvents(JDA jda){
            jda.addEventListener(
                    new AramListener(),
                    new CommandListener(),
                    new MessageListener()
            );
        }
}
