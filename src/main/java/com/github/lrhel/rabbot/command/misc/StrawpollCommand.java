package com.github.lrhel.rabbot.command.misc;

import com.samuelmaddock.strawpollwrapper.StrawPoll;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.util.event.ListenerManager;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class StrawpollCommand implements CommandExecutor {
	@Command(aliases = {"strawpoll"}, privateMessages = false, description = "Make a strawpoll!", showInHelpPage = false, async = true)
	public String onStrawpollCommand(User user, String[] args, TextChannel textChannel) {
		if(args.length == 0) {
			return showHelpMessage();
		}

		switch (args[0].toLowerCase()) {
			case "new":
			case "add":
				strawpoll(user, textChannel);
				break;
			case "check":
				check(user, textChannel, args);
				break;
			default:
				return showHelpMessage();
		}

		return "";
	}

	private String showHelpMessage() {
		return "**Usage:** ```rb.strawpoll [new | check [id]]]```";
	}

	private void strawpoll(User user, TextChannel textChannel) {
		AtomicReference<String> title = new AtomicReference<>();
		AtomicReference<ArrayList<String>> option = new AtomicReference<>(new ArrayList<>(3));
		AtomicReference<Boolean> waiting = new AtomicReference<>(true);
		AtomicReference<ListenerManager> listenerManager = new AtomicReference<>();
		AtomicReference<Boolean> breaking = new AtomicReference<>(false);
		StrawPoll strawPoll;
		StringBuilder response = new StringBuilder();

		textChannel.sendMessage("The titel of the Strawpoll?").join();
		listenerManager.set(textChannel.addMessageCreateListener(messageCreateEvent -> {
			Message message = messageCreateEvent.getMessage();
			if(message.getUserAuthor().isPresent() && message.getUserAuthor().get().getId() == user.getId()) {
				title.set(message.getContent());
				waiting.set(false);
				listenerManager.get().remove();
			}
		}).removeAfter(3, TimeUnit.MINUTES));
		listenerManager.get().addRemoveHandler(() -> waiting.set(false));

		while (waiting.get()) { Thread.onSpinWait(); }

		//Option part
		textChannel.sendMessage("Now we will handle the options, say `STOP` if you don't want to add any more options");
		for(int i = 1; i <= 50; i++) {
			AtomicReference<Boolean> forWaiting = new AtomicReference<>(true);
			textChannel.sendMessage("Option #" + i + " ?").join();
			listenerManager.set(textChannel.addMessageCreateListener(messageCreateEvent -> {
				Message message = messageCreateEvent.getMessage();
				if(message.getUserAuthor().isPresent() && message.getUserAuthor().get().getId() == user.getId()) {
					if (message.getContent().equalsIgnoreCase("stop")) {
						breaking.set(true);
					}
					else {
						option.get().add(message.getContent());
					}
					forWaiting.set(false);
					listenerManager.get().remove();
				}
			}).removeAfter(3, TimeUnit.MINUTES));
			listenerManager.get().addRemoveHandler(() -> forWaiting.set(false));

			while (forWaiting.get()) { Thread.onSpinWait(); }

			if(breaking.get()) {
				break;
			}
		}

		if(option.get().size() == 0) {
			return;
		}

		//Creating the Strawpoll part
		strawPoll = new StrawPoll(title.get(), option.get());
		strawPoll.create();

		response.append("Your StrawPoll has been generated\n")
				.append("```\n")

				.append("ID: ")
				.append(strawPoll.getId())
				.append("\n")

				.append("Title: ")
				.append(strawPoll.getTitle())
				.append("\n")
		;

		int i = 1;
		for(String options : strawPoll.getOptions()) {
			response.append("Option #")
					.append(i)
					.append(": ")
					.append(options)
					.append("\n")
			;
			i++;
		}

		response.append("```")

				.append("URL: ")
				.append(strawPoll.getPollURL())
				.append("\n")
		;

		textChannel.sendMessage(response.toString());
	}
	private void check(User user, TextChannel textChannel, String[] args) {}
}
