package com.github.lrhel.rabbot.command.misc;

import com.github.lrhel.rabbot.config.Config;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ShitpostingCommand implements CommandExecutor {

	@Command(aliases = {"shitposting", "shitpost", "sp"}, description = "Shitposting!", privateMessages = false)
	public String onShitpostingCommand(String[] arg, TextChannel ch, User usr) {
		int nbOfShitposting = 0;
		String path = "/home/koala/RabBot/shitposting.txt";
		if(arg.length > 0) {
			if(arg[0].equals("add") && (usr.isBotOwner() || usr.getIdAsString().contentEquals(Config.SMATHID.toString()) || usr.getIdAsString().contentEquals(Config.THUGA.toString()))) {
				try {
					FileWriter fw = new FileWriter(new File(path), true);
					for(int i = 1; i < arg.length; i++) {
						fw.write(arg[i] + " ");
					}
					fw.write("\n");
					fw.close();
					ch.sendMessage("Shitposting added!");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			try {
				Random rng = new Random(System.currentTimeMillis());
				List<String> line = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)))).lines().collect(Collectors.toList());
				return line.get(rng.nextInt(line.size()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return "";
	}
}
