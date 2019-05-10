package com.github.lrhel.rabbot.command.admin;

import java.io.IOException;

import de.kaleidox.javacord.util.commands.Command;

import org.javacord.api.entity.user.User;

public class RestartCommand {
    @Command(enablePrivateChat = false, description = "Restart RabBot!", shownInHelpCommand = false)
    public String restart(User user) {
        if (user.isBot()) {
            return "";
        }

        if (user.isBotOwner()) {
            new Thread(() -> {
                try {
                    new ProcessBuilder("/bin/sh", "restart.sh").start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            return "Restarting... :thinking: :rabbit:";
        }
        return "";
    }
}