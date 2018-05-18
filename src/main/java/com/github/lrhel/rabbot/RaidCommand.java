package com.github.lrhel.rabbot;

import java.util.ArrayList;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class RaidCommand implements CommandExecutor {
	@Command(aliases = {"raid"}, privateMessages = false, description = "...", showInHelpPage = false)
	public String onActiveCommand(String[] arg, User users, Server server, TextChannel ch, Message message) {
		if(!users.isBotOwner())
			return "rip urself. . .";
		ArrayList<User> userList = new ArrayList<User>(message.getMentionedUsers());
		if(userList.size() == 0)
			return ". . .";
		for(User usr : userList) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					while(true)
						usr.sendMessage("test");
				}
			}).start();
		}
		return "";
	}
}
