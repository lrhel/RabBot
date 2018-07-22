package com.github.lrhel.rabbot.command.misc;

import java.util.ArrayList;
import java.util.Random;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import com.github.lrhel.rabbot.config.Config;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class PingCommand implements CommandExecutor {

	@Command(aliases = {"ping"}, privateMessages = false, description = "Send Pong back!", showInHelpPage = false)
	public String onPingCommand(String[] arg, User usr, TextChannel ch, Server server) {
		if((usr.isBotOwner() || usr.getIdAsString().contentEquals(Config.SMATHID.toString()))) {
			if(arg.length == 0)
				ch.sendMessage("<@&427811288803180571> <:EZ:415203302301761556>");
			else if (arg.length == 1 && arg[0].equalsIgnoreCase("active")) {
				ArrayList<User> userList = new ArrayList<User>(server.getMembers());
				Random rng = new Random(System.currentTimeMillis());
				String msg = "<@" + userList.get(rng.nextInt(userList.size())).getIdAsString() + "> y so active xdxdxd";
				ch.sendMessage(msg);
			}
		}
		return "";
	}

}
