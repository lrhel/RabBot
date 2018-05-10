package com.github.lrhel.rabbot;

import java.util.ArrayList;
import java.util.Random;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class ActiveCommand	implements CommandExecutor {
		@Command(aliases = {"active"}, privateMessages = false, description = "Ping someone and send active message!", showInHelpPage = false)
		public String onActiveCommand(String[] arg, Server server, TextChannel ch, Message message) {
			Random rng = new Random(System.currentTimeMillis());
			if(arg.length == 0) {
				ArrayList<User> userList = new ArrayList<User>(server.getMembers());
				String msg = "<@" + userList.get(rng.nextInt(userList.size())).getIdAsString() + "> y so active xdxdxd";
				ch.sendMessage(msg);
			} else if(arg.length == 1) {
				ArrayList<User> userList = new ArrayList<User>(message.getMentionedUsers());
				String msg;
				if(userList.size() != 0) {
					for(User usr : userList) {
						msg = usr.getMentionTag() + " y so active xdxdxd";
						ch.sendMessage(msg);
					}
				}
				else {
					userList = new ArrayList<User>(server.getMembers());
					for(User usr : userList) {
						if(usr.getName().toLowerCase().contains(arg[0].toLowerCase()) || (usr.getNickname(server).isPresent() && usr.getNickname(server).get().toLowerCase().contains(arg[0].toLowerCase()))) {
							msg = usr.getMentionTag() + " y so active xdxdxd";
							ch.sendMessage(msg);
							break;
						}
					}
				}
			}
			return "";
		}
}
