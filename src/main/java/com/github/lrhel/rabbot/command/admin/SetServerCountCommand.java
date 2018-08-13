package com.github.lrhel.rabbot.command.admin;

import com.github.lrhel.rabbot.Main;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.discordbots.api.client.DiscordBotListAPI;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;

public class SetServerCountCommand implements CommandExecutor {
    DiscordApi api;
    DiscordBotListAPI dblapi;

    public SetServerCountCommand(DiscordBotListAPI dblapi, DiscordApi api) {
        this.dblapi = dblapi;
        this.api = api;
    }

    @Command(aliases = {"setservercount"})
    public String onSetServerCountCommand(User user) {
        if (user.isBot()) { return ""; }

        if(user.isBotOwner()) {
            Main.postServerCount(dblapi, api);
            return "Done";
        }
        return "";
    }
}
