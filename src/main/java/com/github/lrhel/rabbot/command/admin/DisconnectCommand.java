package com.github.lrhel.rabbot.command.admin;

import de.kaleidox.javacord.util.commands.Command;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;

public class DisconnectCommand {
    @Command(aliases = {"disconnect", "disc"}, shownInHelpCommand = false)
    public void onDisconnectCommand(User user, DiscordApi api) {
        if (user.isBot()) {
            return;
        }
        if (user.isBotOwner()) {
            api.disconnect();
        }

    }
}
