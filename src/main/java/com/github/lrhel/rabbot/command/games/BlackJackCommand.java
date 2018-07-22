package com.github.lrhel.rabbot.command.games;

import com.github.lrhel.rabbot.Cards;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.event.ListenerManager;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class BlackJackCommand implements CommandExecutor {
    private static final int INTERVAL = 30 * 1000;
    public static ArrayList<User> using = new ArrayList<>();

    @Command(aliases = {"blackjack"}, description = "BlackJack 21", showInHelpPage = false, async = true)
    public String onBlackJackCommand(User user, TextChannel textChannel, String[] arg, DiscordApi api) {
        int amount;
        Cards deck = new Cards();
        Player player;
        Player bank;

        if(using.contains(user))
            return "";
        else
            using.add(user);
//        if(arg.length != 1)
//            return showHelp();
//        try {
//            amount = Integer.parseInt(arg[0]);
//        } catch (Exception e) {
//            return showHelp();
//        }
//        if(amount <= 0)
//            return showHelp();

        player = new Player(user.getName());
        bank = new Player("Bank");

        //Draw one card each
        player.add(deck.draw());
        bank.add(deck.draw());
        player.add(deck.draw());

        textChannel.sendMessage(player.addEmbed(bank.addEmbed(new EmbedBuilder().setAuthor("BlackJack 21", "", ""))));
        textChannel.sendMessage("`Hit` `Stand`");

        final long timestamp = System.currentTimeMillis();
        ExtendedBoolean stand = new ExtendedBoolean(false);

        AtomicReference<ListenerManager> lm = new AtomicReference<>();
        lm.set(textChannel.addMessageCreateListener(e -> {
            if(e.getMessage().getUserAuthor().get().getId() == user.getId()) {
                if(e.getMessage().getContent().equalsIgnoreCase("hit")) {
                    player.add(deck.draw());
                    if(player.getTotalValue() > 21) {
                        stand.set(true);
                        lm.get().remove();
                    }
                    if(!stand.is()) {
                        textChannel.sendMessage(player.addEmbed(bank.addEmbed(new EmbedBuilder().setAuthor("BlackJack 21", "", ""))));
                        textChannel.sendMessage("`Hit` `Stand`");
                    }
                }
                else if(e.getMessage().getContent().equalsIgnoreCase("stand")) {
                    stand.set(true);
                    lm.get().remove();
                }
            }
        }).removeAfter(INTERVAL, TimeUnit.MILLISECONDS));

        while(!stand.is()) {
            Thread.onSpinWait();
        }

        lm.get().remove();

        bank.add(deck.draw());

        while (bank.getTotalValue() <= 16) {
            if(bank.getTotalValue() > player.getTotalValue() || player.getTotalValue() > 21)
                break;
            bank.add(deck.draw());
        }
        textChannel.sendMessage(player.addEmbed(bank.addEmbed(new EmbedBuilder().setAuthor("BlackJack 21", "", ""))));
        String winner;
        int playerGame = player.getTotalValue();
        int bankGame = bank.getTotalValue();

        if(playerGame > 21) {
            winner = "You lose!";
        }
        else if(playerGame == bankGame) {
            winner = "Draw!";
        }
        else if(bankGame > 21) {
            winner = "You win!";
        }
        else if(bankGame > playerGame) {
            winner = "You lose!";
        }
        else {
            winner = "You win!";
        }
        using.remove(user);
        return winner;
    }

    private String showHelp() {
        return "**Usage:**\n```Rb.blackjack [amount]```";
    }


    private class Player {
        private ArrayList<Cards.Card> hand;
        private String name;

        public Player() {
            hand = new ArrayList<>();
        }

        public Player(String username) {
            this();
            this.name = username;
        }

        public void add(Cards.Card card) {
            this.hand.add(card);
        }

        public int getTotalValue() {
            int total = 0;

            for (Cards.Card card : hand) {
                total += card.getValue() >= 10 ? 10 : card.getValue();
            }

            for (Cards.Card card : hand) {
                if (card.getValue() == 1 && total <= 11)
                    total += 10;
            }

            return total;
        }

        public String getName() {
            return name;
        }

        public EmbedBuilder getEmbed(){
            return new EmbedBuilder().setAuthor("BlackJack 21", "", "");
        }

        public synchronized EmbedBuilder addEmbed(EmbedBuilder embedBuilder) {
            return embedBuilder.addField(this.getName() + "'s hand", this.toString() + "\n").addInlineField("Total: " + String.valueOf(this.getTotalValue()), "\u200b");
        }

        @Override
        public synchronized String toString(){
            String string = "";

            for(Cards.Card card : hand)
                string += card.toString() + "\n";
            return string;
        }

    }

    private class ExtendedBoolean {
        boolean bool;

        public ExtendedBoolean(boolean value) {
            this.set(value);
        }

        public void set(boolean bool) {
            this.bool = bool;
        }

        public boolean is() {
            return this.bool;
        }
    }
}
