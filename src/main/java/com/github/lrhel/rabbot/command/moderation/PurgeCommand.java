package com.github.lrhel.rabbot.command.moderation;

import java.util.ArrayList;

import de.kaleidox.javacord.util.commands.Command;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

public class PurgeCommand {
    @Command(aliases = {"purge", "bulk"}, description = "Clean text channels")
    public String onPurgeCommand(User user, Server server, TextChannel textChannel, String[] arg, Message message) {
        if (user.isBot()) {
            return "";
        }

        Permissions userPermission = server.getPermissions(user);

        if (userPermission.getAllowedPermission().contains(PermissionType.MANAGE_MESSAGES)) {
            int nbOfMsgToDelete;
            if (arg.length == 1) {
                try {
                    nbOfMsgToDelete = Integer.parseInt(arg[0]);
                } catch (Exception e) {
                    nbOfMsgToDelete = 0;
                }
                if (nbOfMsgToDelete < 0)
                    return "The amount of message to delete should be positive";
                else if (nbOfMsgToDelete == 0) {
                    ArrayList<User> userToDeleteMessageList = new ArrayList<>(message.getMentionedUsers());
                    for (User userToDeleteMessage : userToDeleteMessageList) {
                        textChannel.bulkDelete(textChannel.getMessagesBeforeAsStream(message).filter(m -> m.getUserAuthor().get().getId() == userToDeleteMessage.getId()).limit(100)::iterator).join();
                    }
                    message.delete().join();
                } else {
                    textChannel.bulkDelete(textChannel.getMessagesAsStream().limit(++nbOfMsgToDelete)::iterator).join();
                }
            }
        }
        return "";
    }
}
