package com.github.lrhel.rabbot.command.admin;

import java.util.ArrayList;

import de.kaleidox.javacord.util.commands.Command;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

public class GetServerCommand {
    @Command(aliases = {"getserver", "gs"}, description = "Get List of all Server", shownInHelpCommand = false)
    public String onGetServerCommand(User user, DiscordApi api) {
        if (user.isBot()) {
            return "";
        }

        if (!user.isBotOwner())
            return "";

        ArrayList<Server> servers = new ArrayList<>(api.getServers());
        String list = "```\n";
        for (Server server : servers) {
            list += server.getName().length() > 16 ? server.getName().subSequence(0, 16) : server.getName();
            for (int i = 16 - server.getName().length(); i > 0; i--)
                list += " ";
//            list += " | ";
//            try {
//                list += server.getInvites().join().iterator().next().getUrl().toExternalForm();
//            } catch (Exception e){
//                list += "No invitation";
//            }
            list += "\n";
        }
        list += "```";
        return list;
    }
}
