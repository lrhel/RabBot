package com.github.lrhel.rabbot.command.admin;

import java.io.IOException;

import de.kaleidox.javacord.util.commands.Command;

import org.javacord.api.entity.user.User;

public class UpdateCommand {
    @Command(enablePrivateChat = false, description = "Update RabBot!", shownInHelpCommand = false)
    public String updates(User user) {
        if (user.isBot()) {
            return "";
        }

        if (user.isBotOwner()) {
            new Thread(() -> {
                try {
                    new ProcessBuilder("/bin/sh", "update.sh").start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            return "Updating... :thinking: :rabbit:";
        }
        return "";
    }
}
