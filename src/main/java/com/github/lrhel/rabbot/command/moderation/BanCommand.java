package com.github.lrhel.rabbot.command.moderation;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;

public class BanCommand implements CommandExecutor {
    @Command(aliases = {"ban"}, description = "Ban a user(s)")
    public String banCommand(User user, Message message, Server server, String[] arg) {
        if (user.isBot()) { return ""; }

        if(!server.canYouBanUsers())
            return "Rabbot cannot ban users!";
        if(!server.canBanUsers(user))
            return "You cannot ban users!";

        ArrayList<User> userToBanList;
        userToBanList = new ArrayList<>(message.getMentionedUsers());

        if(userToBanList.size() == 0)
            return "No user mentioned";
        String reason = String.join(" ", arg);

        for(User userToBan : userToBanList) {
            reason = reason.replace(userToBan.getMentionTag(), "");
            reason = reason.replace(userToBan.getNicknameMentionTag(), "");
        }

        for(User userToBan : userToBanList){
            if(!server.canYouBanUser(userToBan)) {
                message.getServerTextChannel().get().sendMessage("Rabbot cannot ban " + userToBan.getMentionTag());
            }
            else if(server.canBanUser(user, userToBan)) {
                server.banUser(userToBan, 7, reason).join();
                message.getServerTextChannel().get().sendMessage(userToBan.getMentionTag() + " banned")
                .join();
            }
            else
                message.getServerTextChannel().get().sendMessage("Something went wrong ... ");
        }

        return "";
    }
}
