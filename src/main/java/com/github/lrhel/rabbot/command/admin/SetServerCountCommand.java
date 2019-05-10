package com.github.lrhel.rabbot.command.admin;

import de.kaleidox.javacord.util.commands.Command;

import com.github.lrhel.rabbot.Main;
import org.discordbots.api.client.DiscordBotListAPI;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;

public class SetServerCountCommand {
    DiscordApi api;
    DiscordBotListAPI dblapi;

    public SetServerCountCommand(DiscordBotListAPI dblapi, DiscordApi api) {
        this.dblapi = dblapi;
        this.api = api;
    }

    @Command
    public String setservercount(User user) {
        if (user.isBot()) {
            return "";
        }

        if (user.isBotOwner()) {
            Main.postServerCount(dblapi, api);
            return "Done";
        }
        return "";
    }
}
