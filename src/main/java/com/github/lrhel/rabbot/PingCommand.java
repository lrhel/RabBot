package com.github.lrhel.rabbot;

import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class PingCommand implements CommandExecutor {

	@Command(aliases = {"ping"}, privateMessages = false, description = "Send Pong back!")
	public String onPingCommand(User user) {
		if(user.isBotOwner())
			return "";
	    return "Pong!";
	}

}
