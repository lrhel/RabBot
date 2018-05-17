package com.github.lrhel.rabbot;

import java.io.IOException;

import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class RestartCommand implements CommandExecutor {
	@Command(aliases = {"restart"}, privateMessages = false, description = "Restart RabBot!", showInHelpPage = false)
	public String onRestartCommand(User usr) {
		if(usr.isBotOwner()) {
			new Thread(new Runnable() {
				public void run(){
					try {
						new ProcessBuilder("/bin/sh", "restart.sh").start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
			return "Restarting... :thinking: :rabbit:";
		}
		return "Rip you're not the Bot Owner";
	}
}
