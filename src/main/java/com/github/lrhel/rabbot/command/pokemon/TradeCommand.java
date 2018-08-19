package com.github.lrhel.rabbot.command.pokemon;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.event.ListenerManager;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class TradeCommand implements CommandExecutor {
    @Command(aliases = {"trade"}, description = "Trade Pokemon", async = true)
    public void onTradeCommand(User user, TextChannel textChannel) {
        if(user.isBot()) { return; }

        AtomicReference<Boolean> response = new AtomicReference<>(false);
        AtomicReference<Boolean> response1 = new AtomicReference<>(false);
        AtomicReference<Boolean> response2 = new AtomicReference<>(false);
        AtomicReference<ListenerManager> lm = new AtomicReference<>();
        AtomicReference<User> userToTrade = new AtomicReference<>();
        AtomicReference<Integer> pkmnA = new AtomicReference<>();
        AtomicReference<Integer> pkmnB = new AtomicReference<>();

        textChannel.sendMessage("With whom you wanna trade a Pokemon?").join();
        lm.set(textChannel.addMessageCreateListener(messageCreateEvent -> {
            Message message = messageCreateEvent.getMessage();
            if (message.getUserAuthor().get().getId() == user.getId() && message.getMentionedUsers().size() >= 1) {
                userToTrade.set(message.getMentionedUsers().get(0));
                response.set(true);
                lm.get().remove();
            }
        }).removeAfter(5, TimeUnit.MINUTES));
        lm.get().addRemoveHandler(() -> response.set(true));
        while (!response.get()) {
            Thread.onSpinWait();
        }

        textChannel.sendMessage(user.getMentionTag() + " which Pokemon you wanna trade? (please specify a number");
        lm.set(textChannel.addMessageCreateListener(messageCreateEvent -> {
            Message message = messageCreateEvent.getMessage();
            if (message.getUserAuthor().get().getId() == user.getId()) {
                try {
                    pkmnA.set(Integer.parseInt(message.getContent()));
                    response1.set(true);
                    lm.get().remove();
                } catch (Exception e) { }
            }
        }).removeAfter(5, TimeUnit.MINUTES));
        lm.get().addRemoveHandler(() -> response.set(true));
        while (!response1.get()) {
            Thread.onSpinWait();
        }

        textChannel.sendMessage(userToTrade.get().getMentionTag() + " which Pokemon you wanna trade? (please specify a number");
        lm.set(textChannel.addMessageCreateListener(messageCreateEvent -> {
            Message message = messageCreateEvent.getMessage();
            if (message.getUserAuthor().get().getId() == userToTrade.get().getId()) {
                try {
                    pkmnB.set(Integer.parseInt(message.getContent()));
                    response2.set(true);
                    lm.get().remove();
                } catch (Exception e) { }
            }
        }).removeAfter(5, TimeUnit.MINUTES));
        lm.get().addRemoveHandler(() -> response.set(true));
        while (!response2.get()) {
            Thread.onSpinWait();
        }

        try {
            RabbotPokemon.tradePokemon(user, pkmnA.get(), userToTrade.get(), pkmnB.get());
        } catch (Exception e) { e.printStackTrace(); textChannel.sendMessage("something went wrong"); }
    }
}
