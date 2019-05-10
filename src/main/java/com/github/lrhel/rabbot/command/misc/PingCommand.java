package com.github.lrhel.rabbot.command.misc;

import java.time.Duration;
import java.time.Instant;

import de.kaleidox.javacord.util.commands.Command;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

public class PingCommand {

    @Command(enablePrivateChat = false, description = "Send Pong back!", shownInHelpCommand = false)
    public void ping(String[] arg, User user, TextChannel ch, Server server, Message message) {
        if (user.isBot()) {
            return;
        }

        Instant now = Instant.now();

        Instant msgTimestamp = message.getCreationTimestamp();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("**PING** in: ").append(Duration.between(msgTimestamp, now).toNanos() / 1000000.0).append("ms\n");

        ch.sendMessage(stringBuilder.toString()).whenComplete((m, e) -> {
            m.edit(stringBuilder.toString() + "**PONG** in " + (Duration.between(msgTimestamp, m.getCreationTimestamp()).toNanos() / 1000000.0) + "ms\n");
        }).join();


    }

}
