package com.github.lrhel.rabbot.command.admin;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;

public class DisconnectCommand implements CommandExecutor {
    @Command(aliases = {"disconnect", "disc"}, showInHelpPage = false)
    public String onDisconnectCommand(User user, DiscordApi api){
        if(!user.isBotOwner())
            return "";
        api.disconnect();
        return "Bot disconnected";
    }

}
