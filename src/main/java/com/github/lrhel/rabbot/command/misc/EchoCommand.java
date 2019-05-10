package com.github.lrhel.rabbot.command.misc;

import de.kaleidox.javacord.util.commands.Command;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

public class EchoCommand {
    @Command(aliases = {"echo"}, enablePrivateChat = false, description = "Echo what I say!", shownInHelpCommand = false)
    public String onEchoCommand(User user, String[] arg, Message msg) {
        if (user.isBot()) {
            return "";
        }

        if (msg.canYouDelete())
            msg.delete();

        return String.join(" ", arg);
    }
}
