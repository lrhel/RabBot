package com.github.lrhel.rabbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class UpdateCommand implements CommandExecutor {
	@Command(aliases = {"update"}, privateMessages = false, description = "Update RabBot!", showInHelpPage = false)
	public String onUpdateCommand(User usr) {
		if(usr.isBotOwner()) {
			new Thread(new Runnable() {
				public void run(){
					try {
						new ProcessBuilder("/bin/sh", "update.sh").start();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
			return "Updating... :thinking:";
		}
		return "Rip you're not the Bot Owner";
	}
}
