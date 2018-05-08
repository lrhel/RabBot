package com.github.lrhel.rabbot;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;

import com.github.lrhel.rabbot.config.Config;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class PingCommand implements CommandExecutor {

	@Command(aliases = {"ping"}, privateMessages = false, description = "Send Pong back!", showInHelpPage = false)
	public String onPingCommand(User usr, TextChannel ch) {
		if((usr.isBotOwner() || usr.getIdAsString().contentEquals(Config.SMATHID.toString()))) {
			ch.sendMessage("<@&427811288803180571> <:EZ:415203302301761556>");
		}
		return "";
	}

}
