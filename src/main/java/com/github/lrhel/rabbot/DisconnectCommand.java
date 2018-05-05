package com.github.lrhel.rabbot;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class DisconnectCommand implements MessageCreateListener {

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		String message = event.getMessage().getContent();
		if(event.getMessage().getAuthor().isBotOwner()
				&& (message.equalsIgnoreCase("!disconnect")
						|| message.equalsIgnoreCase("!disco"))) {
			event.getApi().disconnect();
			System.out.println("Bot disconnected");
		}
	}

}
