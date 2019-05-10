package com.github.lrhel.rabbot.command.moderation;

import java.util.ArrayList;

import de.kaleidox.javacord.util.commands.Command;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

public class MuteCommand {
    @Command(description = "Add \"mute\" role(s) to the user")
    public String mute(User user, Message message, Server server, String[] arg) {
        if (user.isBot()) {
            return "";
        }

        Permissions userPerm = server.getPermissions(user);

        if (!server.canYouManageRoles())
            return "Rabbot cannot give mute roll";

        if (!userPerm.getAllowedPermission().contains(PermissionType.KICK_MEMBERS))
            return "You cannot mute users";

        ArrayList<User> userToMuteList = new ArrayList<>(message.getMentionedUsers());

        if (userToMuteList.isEmpty())
            return "No user mentioned!";

        StringBuilder error = new StringBuilder();
        String reason = String.join(" ", arg);

        for (User userToMute : userToMuteList) {
            reason = reason.replace(userToMute.getMentionTag(), "");
            reason = reason.replace(userToMute.getNicknameMentionTag(), "");
        }

        for (User userToMute : userToMuteList) {
            if (!server.canKickUser(user, userToMute)) {
                error.append("You cannot mute ");
                error.append(userToMute.getMentionTag());
                error.append("\n");
            }
            for (Role role : server.getRoles())
                if (role.getName().toLowerCase().contains("mute"))
                    userToMute.addRole(role, reason).join();
        }

        return error.toString();
    }
}
