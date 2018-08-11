package com.github.lrhel.rabbot.command.misc;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;

public class InviteCommand implements CommandExecutor {
    @Command(aliases = {"invite", "invites"})
    public String onInviteCommand(DiscordApi api) {
        return api.createBotInvite(new PermissionsBuilder().setAllowed(
                PermissionType.READ_MESSAGES, PermissionType.ATTACH_FILE,
                PermissionType.SEND_MESSAGES, PermissionType.EMBED_LINKS,
                PermissionType.ADD_REACTIONS, PermissionType.READ_MESSAGE_HISTORY
        ).build());
    }
}
