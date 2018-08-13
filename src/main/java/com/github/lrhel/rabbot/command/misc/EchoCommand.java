package com.github.lrhel.rabbot.command.misc;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

public class EchoCommand implements CommandExecutor {
	@Command(aliases = {"echo"}, privateMessages = false, description = "Echo what I say!", showInHelpPage = false)
	public String onEchoCommand(User user, String[] arg, Message msg) {
		if (user.isBot()) { return ""; }

		if(msg.canYouDelete())
			msg.delete();

		return String.join(" ", arg);
	}
}
