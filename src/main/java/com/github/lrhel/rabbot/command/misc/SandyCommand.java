package com.github.lrhel.rabbot.command.misc;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.util.Random;

public class SandyCommand implements CommandExecutor {

	@Command(aliases = {"sandy"}, description = "Complimente Sandy!", privateMessages = false, showInHelpPage = false)
	public String onSandyCommand(User user, TextChannel ch) {
		if (user.isBot()) { return ""; }

		MessageBuilder mbuilder = new MessageBuilder();
		EmbedBuilder ebuilder = new EmbedBuilder();
		ebuilder.setTitle("WOW!!")
				.setDescription(SandyCommand.getCompliment());
		mbuilder.setEmbed(ebuilder);
		ch.sendMessage(ebuilder);
		return  "";
	}
	
	private static String getCompliment() {
		String table[] = {"belle", "gentille", "sympatoch", "mignonne", "magnifique", "divine", "authentique", "geniiiaaaale", "kawaiiii"};
		Random rng = new Random(System.currentTimeMillis());
		return "Sandy est la plus " + table[rng.nextInt(table.length)];
	}

}

	