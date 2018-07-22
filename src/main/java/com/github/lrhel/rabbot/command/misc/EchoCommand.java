package com.github.lrhel.rabbot.command.misc;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class EchoCommand implements CommandExecutor {
	@Command(aliases = {"echo"}, privateMessages = false, description = "Echo what I say!", showInHelpPage = false)
	public String onEchoCommand(String[] arg, TextChannel ch, User usr) {
		return String.join(" ", arg);
	}
}
