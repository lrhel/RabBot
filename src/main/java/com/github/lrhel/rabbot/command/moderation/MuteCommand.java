package com.github.lrhel.rabbot.command.moderation;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;

public class MuteCommand implements CommandExecutor {
    @Command(aliases = {"mute"}, showInHelpPage = false)
    public String muteCommand(User user, Message message, Server server) {
        Permissions userPerm = server.getPermissions(user);

        if(!userPerm.getAllowedPermission().contains(PermissionType.KICK_MEMBERS))
            return "You cannot mute users";

        ArrayList<User> userToMuteList = new ArrayList<>(message.getMentionedUsers());

        if(userToMuteList.isEmpty())
            return "No user mentioned!";

//        for(User userToMute : userToMuteList) {
//
//        }

        return "";
    }
}
