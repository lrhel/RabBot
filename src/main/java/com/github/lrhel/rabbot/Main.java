package com.github.lrhel.rabbot;

import de.kaleidox.javacord.util.commands.CommandHandler;

import com.github.lrhel.rabbot.command.Commands;
import com.github.lrhel.rabbot.config.Const;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.discordbots.api.client.DiscordBotListAPI;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;


public class Main {

    public static void main(String[] args) {
        //set logger and tracer
        FallbackLoggerConfiguration.setDebug(true);
        FallbackLoggerConfiguration.setTrace(false);

        DiscordApi api = new DiscordApiBuilder().setToken(Const.TOKEN).login().join();
        System.out.println("Logged in!");
        System.out.println(api.createBotInvite(new PermissionsBuilder().setAllowed(
                PermissionType.READ_MESSAGES,
                PermissionType.ATTACH_FILE,
                PermissionType.SEND_MESSAGES,
                PermissionType.EMBED_LINKS,
                PermissionType.ADD_REACTIONS,
                PermissionType.READ_MESSAGE_HISTORY
        ).build()));

        DiscordBotListAPI discordBotListAPI = new DiscordBotListAPI.Builder()
                .token(Const.DISCORDLIST_TOKEN)
                .botId(String.valueOf(Const.BOT_ID))
                .build();

        //Command Handler & Option
        CommandHandler cmd = new CommandHandler(api);
        cmd.prefixes = new String[]{"rb."};
        cmd.useDefaultHelp(null); // null to use DefaultEmbedFactory class
        api.updateActivity("rb.help");

        cmd.registerCommands(Commands.class.getPackage());

        //Join and Leave
        api.addServerJoinListener(event -> {
            System.out.println("Joined server " + event.getServer().getName());
            postServerCount(discordBotListAPI, api);
        });

        api.addServerLeaveListener(event -> {
            System.out.println("Leaved server " + event.getServer().getName());
            postServerCount(discordBotListAPI, api);
        });

    }

    public static void postServerCount(DiscordBotListAPI discordBotListAPI, DiscordApi discordApi) {
        discordBotListAPI.setStats(discordApi.getServers().size());

        //discord.pw
        OkHttpClient client = new OkHttpClient();
        StringBuilder urlPw = new StringBuilder("https://bots.discord.pw/api")
                .append("/bots")
                .append("/").append(Const.BOT_ID).append("/");
        urlPw.append("stats/");

        StringBuilder urlBfs = new StringBuilder("https://botsfordiscord.com/api/v1/")
                .append("bots")
                .append("/").append(Const.BOT_ID).append("/");
        StringBuilder json = new StringBuilder()
                .append("{" + "    \"server_count\": ")
                .append(discordApi.getServers().size())
                .append("}");
        try {
            Request request = new Request.Builder()
                    .header("Authorization", Const.PW_TOKEN)
                    .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                            json.toString()))
                    .url(urlPw.toString())
                    .build();
            client.newCall(request).execute();

            request = new Request.Builder()
                    .header("Authorization", Const.BFD_TOKEN)
                    .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                            json.toString()))
                    .url(urlBfs.toString())
                    .build();
            client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
