package com.github.lrhel.rabbot.command.misc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;

import com.github.lrhel.rabbot.config.Config;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class CopypastaCommand implements CommandExecutor {

	@Command(aliases = {"copypasta", "copy", "cp"}, description = "Copypasta!", privateMessages = false)
	public String onShitpostingCommand(String[] arg, TextChannel ch, User usr) {
		int nbOfShitposting = 0;
		String path = "/home/koala/RabBot/copypasta.txt";
		if(arg.length > 0) {
			try {
				nbOfShitposting = Integer.parseInt(arg[0]);
			} catch (NumberFormatException e) {
				nbOfShitposting = 0;
			}
			if (nbOfShitposting == 0) {
				if(arg[0].equals("add") && (usr.isBotOwner() || usr.getIdAsString().contentEquals(Config.SMATHID.toString()) || usr.getIdAsString().contentEquals(Config.THUGA.toString()))) {
					try {
						FileWriter fw = new FileWriter(new File(path), true);
						for(int i = 1; i < arg.length; i++) {
							fw.write(arg[i] + " ");
						}
						fw.write("\n");
						fw.close();
						ch.sendMessage("copypasta added!");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			try {
				Random rng = new Random(System.currentTimeMillis());
				int count = getLineCount(new File(path));
				Scanner sc = new Scanner(new File(path));
				int j = 0;
				int random = rng.nextInt(count);
				while (sc.hasNextLine()) {
					String msg = sc.nextLine();
					if(random == j) {
						ch.sendMessage(msg);
						break;
					}
					j++;
				}
				sc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return "";
	}
	public static int getLineCount(File file) throws IOException {

		try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(file), 1024)) {

			byte[] c = new byte[1024];
			boolean empty = true,
					lastEmpty = false;
			int count = 0;
			int read;
			while ((read = is.read(c)) != -1) {
				for (int i = 0; i < read; i++) {
					if (c[i] == '\n') {
						count++;
						lastEmpty = true;
					} else if (lastEmpty) {
						lastEmpty = false;
					}
				}
				empty = false;
			}

			if (!empty) {
				if (count == 0) {
					count = 1;
				} else if (!lastEmpty) {
					count++;
				}
			}

			return count;
		}
	}
}
