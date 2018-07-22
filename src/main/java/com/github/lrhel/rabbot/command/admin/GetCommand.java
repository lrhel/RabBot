package com.github.lrhel.rabbot.command.admin;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;

public class GetCommand implements CommandExecutor {
    @Command(aliases = {"get"}, showInHelpPage = false)
    public String onGetCommand(User user, String[] arg, DiscordApi api){
        if (!user.isBotOwner() || arg.length != 1)
            return "";
        switch (arg[0]) {
            case "server":
                ArrayList<Server> servers = new ArrayList<>(api.getServers());
                String list = "```\n";
                for(Server server : servers){
                    list += server.getName().length() > 16 ? server.getName().subSequence(0, 16) : server.getName();
                    for (int i = 16 - server.getName().length(); i > 0; i--)
                        list += " ";
                    list += " | ";
                    try {
                        list += server.getInvites().join().iterator().next().getUrl().toExternalForm();
                    } catch (Exception e){
                        list += "No invitation";
                    }
                    list += "\n";
                }
                list += "```";
                return list;
            case "inv":
            case "invite":
                return api.createBotInvite();
        }
        return "";
    }
}
