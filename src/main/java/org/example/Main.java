package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Main {
    private static Dotenv config;
    public static void main(String[] args) {
        config = Dotenv.configure().ignoreIfMissing().load();
        String token = config.get("TOKEN");

        JDA jda = JDABuilder
                .createDefault(token) // token for discord bot i devolper portal
                .setActivity(Activity.watching("Wednesday 😍")) // setter aktivitet
                .enableIntents(GatewayIntent.MESSAGE_CONTENT) // lese meldinger
                .enableIntents(GatewayIntent.GUILD_PRESENCES) // lese brukers aktivitet
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableCache(CacheFlag.ACTIVITY) // lese brukers aktivitet
                .setMemberCachePolicy(MemberCachePolicy.ALL)  // cacher alle brukere av botten
                .setChunkingFilter(ChunkingFilter.ALL) // tvinger botten til å cache alle brukere
                .build();

        MyEventListener mml = new MyEventListener();
        jda.addEventListener(mml);
    }
}
