package com.github.lrhel.rabbot.command.misc;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.DiscordApi;

public class InviteCommand implements CommandExecutor {
    @Command(aliases = {"invite", "invites", "inv"})
    public String onInviteCommand(DiscordApi api) {
        return api.createBotInvite();
    }
}
