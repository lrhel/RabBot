package com.github.lrhel.rabbot.command.misc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;

import com.github.lrhel.rabbot.config.Config;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

import static com.github.lrhel.rabbot.command.misc.CopypastaCommand.getLineCount;

public class ShitpostingCommand implements CommandExecutor {

	@Command(aliases = {"shitposting", "shitpost", "sp"}, description = "Shitposting!", privateMessages = false)
	public String onShitpostingCommand(String[] arg, TextChannel ch, User usr) {
		int nbOfShitposting = 0;
		String path = "/home/koala/RabBot/shitposting.txt";
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
						ch.sendMessage("Shitposting added!");
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
					System.out.println(msg);

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
}
