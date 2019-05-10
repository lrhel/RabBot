package com.github.lrhel.rabbot.command.misc;

import de.kaleidox.javacord.util.commands.Command;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.user.User;

public class InviteCommand {
    @Command(aliases = {"invite", "invites"})
    public String onInviteCommand(User user, DiscordApi api) {
        if (user.isBot()) {
            return "";
        }

        return api.createBotInvite(new PermissionsBuilder().setAllowed(
                PermissionType.READ_MESSAGES, PermissionType.ATTACH_FILE,
                PermissionType.SEND_MESSAGES, PermissionType.EMBED_LINKS,
                PermissionType.ADD_REACTIONS, PermissionType.READ_MESSAGE_HISTORY
        ).build());
    }
}
