package com.github.lrhel.rabbot.command.admin;

import com.github.lrhel.rabbot.Money;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;

public class GetCommand implements CommandExecutor {
    @Command(aliases = {"get"}, showInHelpPage = false)
    public String onGetCommand(User user, String[] arg, DiscordApi api, Message message){
        if (!user.isBotOwner() || arg.length == 0)
            return "";
        switch (arg[0]) {
            case "server":
                ArrayList<Server> servers = new ArrayList<>(api.getServers());
                StringBuilder list = new StringBuilder("```\n");
                for(Server server : servers){
                    list.append(server.getName().length() > 16 ? server.getName().subSequence(0, 16) : server.getName());
                    for (int i = 16 - server.getName().length(); i > 0; i--)
                        list.append(" ");
                    list.append(" | ");
                    try {
                        list.append(server.getInvites().join().iterator().next().getUrl().toExternalForm());
                    } catch (Exception e){
                        list.append("No invitation");
                    }
                    list.append("\n");
                }
                list.append("```");
                return list.toString();
            case "inv":
            case "invite":
                return api.createBotInvite();
            case "money":
                StringBuilder sb = new StringBuilder();
                for(User usr : message.getMentionedUsers())
                    sb.append(String.valueOf(Money.getMoney(usr) + "$\n"));
                return sb.toString();
        }
        return "";
    }
}
