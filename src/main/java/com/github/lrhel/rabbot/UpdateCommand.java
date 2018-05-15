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
				Process process = new ProcessBuilder("/bin/sh", "update.sh").start();
				BufferedReader reader = 
						new BufferedReader(new InputStreamReader(process.getInputStream()));
				StringBuilder builder = new StringBuilder();
				String line = null;
				while ( (line = reader.readLine()) != null) {
					builder.append(line);
					builder.append(System.getProperty("line.separator"));
				}
				String result = builder.toString();
				System.out.println(result);
				System.exit(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				}
			}).start();
			return "Updated :thinking:";
		}
		return "";
	}
}
