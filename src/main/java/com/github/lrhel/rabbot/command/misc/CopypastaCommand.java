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

public class CopypastaCommand implements CommandExecutor {

	@Command(aliases = {"copypasta", "copy", "cp"}, description = "Copypasta!", privateMessages = false)
	public String onShitpostingCommand(String[] arg, TextChannel ch, User user) {
		if (user.isBot()) { return ""; }

		String path = "/home/koala/RabBot/copypasta.txt";
		if(arg.length > 0) {
			if(arg[0].equals("add") && (user.isBotOwner() || user.getIdAsString().contentEquals(Config.SMATHID.toString()) || user.getIdAsString().contentEquals(Config.THUGA.toString()))) {
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
		else {
			try {
				Random rng = new Random(System.currentTimeMillis());
				List<String> line = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)))).lines().collect(Collectors.toList());
				return line.get(rng.nextInt(line.size()));
			} catch (Exception e) {
				e.printStackTrace();
				return "";
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
