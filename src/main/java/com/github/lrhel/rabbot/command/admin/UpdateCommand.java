package com.github.lrhel.rabbot.command.admin;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.user.User;

import java.io.IOException;

public class UpdateCommand implements CommandExecutor {
	@Command(aliases = {"update"}, privateMessages = false, description = "Update RabBot!", showInHelpPage = false)
	public String onUpdateCommand(User user) {
		if (user.isBot()) { return ""; }

		if(user.isBotOwner()) {
			new Thread(() -> {
				try {
					new ProcessBuilder("/bin/sh", "update.sh").start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();
			return "Updating... :thinking: :rabbit:";
		}
		return "";
	}
}
