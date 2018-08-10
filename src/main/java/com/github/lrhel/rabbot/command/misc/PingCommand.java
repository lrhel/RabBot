package com.github.lrhel.rabbot.command.misc;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.time.Duration;
import java.time.Instant;

public class PingCommand implements CommandExecutor {

	@Command(aliases = {"ping"}, privateMessages = false, description = "Send Pong back!", showInHelpPage = false)
	public void onPingCommand(String[] arg, User usr, TextChannel ch, Server server, Message message) {
		Instant now = Instant.now();

		Instant msgTimestamp = message.getCreationTimestamp();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("**PING** in: ").append(Duration.between(msgTimestamp, now).toNanos() / 1000000.0).append("ms\n");

		ch.sendMessage(stringBuilder.toString()).whenComplete((m, e) -> {
			m.edit(stringBuilder.toString() + "**PONG** in " + (Duration.between(msgTimestamp, m.getCreationTimestamp()).toNanos() / 1000000.0) + "ms\n");
		}).join();


	}

}
