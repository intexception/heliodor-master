package me.chmvrek;

import me.chmvrek.commands.Exploit;
import me.chmvrek.commands.HelpCommand;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.api.requests.GatewayIntent.*;

public class Main {
    public static final String prefix = "c!";

    public static void main(String[] args) throws Exception {
//        JDABuilder builder = JDABuilder.createLight("ODIxNDYzMTc3MjkyMjgzOTA0.YFEFMw.E2TMLEruzbGcnGtaT3MOhksKL-c").enableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS)
//                .setMemberCachePolicy(MemberCachePolicy.all(MemberCachePolicy.ALL))
//                .setChunkingFilter(ChunkingFilter.ALL);
//        builder.addEventListeners(new HelpCommand());
//        builder.setActivity(Activity.playing("Heliodor.space"));
//        builder.build();
        JDABuilder.createLight("ODIxNDYzMTc3MjkyMjgzOTA0.YFEFMw.E2TMLEruzbGcnGtaT3MOhksKL-c", GUILD_MESSAGES, GUILD_MEMBERS, DIRECT_MESSAGES, GUILD_PRESENCES)
                .addEventListeners(new HelpCommand())
                .enableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS)
                .setMemberCachePolicy(MemberCachePolicy.all(MemberCachePolicy.ALL))
                .setChunkingFilter(ChunkingFilter.ALL)
                .setActivity(Activity.playing("Heliodor"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .build();
    }
}
