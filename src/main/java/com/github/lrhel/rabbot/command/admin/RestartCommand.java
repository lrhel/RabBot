package com.github.lrhel.rabbot.command.admin;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.user.User;

import java.io.IOException;

public class RestartCommand implements CommandExecutor {
    @Command(aliases = {"restart"}, privateMessages = false, description = "Restart RabBot!", showInHelpPage = false)
    public String onRestartCommand(User user) {
        if (user.isBot()) { return ""; }

        if(user.isBotOwner()) {
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