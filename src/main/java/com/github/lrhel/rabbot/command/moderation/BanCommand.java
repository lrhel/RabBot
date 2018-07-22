package com.github.lrhel.rabbot.command.moderation;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;

public class BanCommand implements CommandExecutor {
    @Command(aliases = {"ban"}, showInHelpPage = false)
    public String banCommand(User user, Message message, Server server, DiscordApi api) {
        if(!server.canYouBanUsers())
            return "Rabbot cannot ban users!";
        if(!server.canBanUsers(user))
            return "You cannot ban users!";

        ArrayList<User> userToBanList;
        userToBanList = new ArrayList<>(message.getMentionedUsers());

        if(userToBanList.isEmpty())
            return "No user mentioned";
        for(User userToBan : userToBanList){
            if(!server.canYouBanUser(userToBan)) {
                message.getServerTextChannel().get().sendMessage("Rabbot cannot ban " + userToBan.getMentionTag());
            }
            else if(server.canBanUser(user, userToBan)) {
                server.banUser(userToBan);
                message.getServerTextChannel().get().sendMessage(userToBan.getMentionTag() + " banned");
            }
            else
                message.getServerTextChannel().get().sendMessage("Something went wrong ... ");
        }

        String rest = message.getContent().replace("rb.ban", "");
        for(User usr : userToBanList)
            rest = rest.replace(usr.getMentionTag(), "");
        return rest;

    }
}
