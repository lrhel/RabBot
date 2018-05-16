package com.github.lrhel.rabbot;

import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class StrawpollCommand implements CommandExecutor {
	@Command(aliases = {"strawpoll"}, privateMessages = false, description = "Make a strawpoll!", showInHelpPage = false)
	public String onStrawpollCommand(User usr) {
		return "Not yet implemented";
	}
}
