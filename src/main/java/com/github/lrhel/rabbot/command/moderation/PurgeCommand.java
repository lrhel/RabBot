package com.github.lrhel.rabbot.command.moderation;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;

public class PurgeCommand implements CommandExecutor {
    @Command(aliases = {"purge", "bulk"}, description = "Clean text channels", async = true)
    public String onPurgeCommand(User user, Server server, TextChannel textChannel, String[] arg, Message message) {
        if (user.isBot()) { return ""; }

        Permissions userPermission = server.getPermissions(user);

        if(userPermission.getAllowedPermission().contains(PermissionType.MANAGE_MESSAGES)) {
            int nbOfMsgToDelete;
            if(arg.length == 1) {
                try {
                    nbOfMsgToDelete = Integer.parseInt(arg[0]);
                } catch (Exception e) {
                    nbOfMsgToDelete = 0;
                }
                if(nbOfMsgToDelete < 0)
                    return "The amount of message to delete should be positive";
                else if(nbOfMsgToDelete == 0) {
                    ArrayList<User> userToDeleteMessageList = new ArrayList<>(message.getMentionedUsers());
                    for(User userToDeleteMessage : userToDeleteMessageList) {
                        textChannel.bulkDelete(textChannel.getMessagesBeforeAsStream(message).filter(m -> m.getUserAuthor().get().getId() == userToDeleteMessage.getId()).limit(100)::iterator).join();
                    }
                    message.delete().join();
                }
                else {
                    textChannel.bulkDelete(textChannel.getMessagesAsStream().limit(++nbOfMsgToDelete)::iterator).join();
                }
            }
        }
        return "";
    }
}
