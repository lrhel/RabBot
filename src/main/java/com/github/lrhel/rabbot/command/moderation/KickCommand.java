package com.github.lrhel.rabbot.command.moderation;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;

public class KickCommand implements CommandExecutor {
    @Command(aliases = {"kick"}, description = "Kick a user(s)")
    public String kickCommand(User user, Message message, Server server, String[] arg) {
        if (user.isBot()) { return ""; }

        if(!server.canYouKickUsers())
            return "Rabbot cannot kick users!";
        if(!server.canKickUsers(user))
            return "You cannot kick users!";

        ArrayList<User> userToKickList;
        userToKickList = new ArrayList<>(message.getMentionedUsers());

        if(userToKickList.size() == 0)
            return "No user mentioned";

        String reason = String.join(" ", arg);

        for(User userToKick : userToKickList) {
            reason = reason.replace(userToKick.getMentionTag(), "");
            reason = reason.replace(userToKick.getNicknameMentionTag(), "");
        }

        for(User userToKick : userToKickList){
            if(!server.canYouKickUser(userToKick)) {
                message.getServerTextChannel().get().sendMessage("Rabbot cannot kick " + userToKick.getMentionTag());
            }
            else if(server.canKickUser(user, userToKick)) {
                server.kickUser(userToKick, reason).join();
                message.getServerTextChannel().get().sendMessage(userToKick.getMentionTag() + " kicked");
            }
            else
                message.getServerTextChannel().get().sendMessage("Something went wrong ... ");
        }

        return "";

    }
}
