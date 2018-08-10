package com.github.lrhel.rabbot.command.games;

import com.github.lrhel.rabbot.Cards;
import com.github.lrhel.rabbot.Money;
import com.github.lrhel.rabbot.utility.ExtendedBoolean;
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
    private static ArrayList<User> using = new ArrayList<>();


    private static EmbedBuilder getEmbed(){
        return new EmbedBuilder().setAuthor("BlackJack 21", "", "");
    }

    @Command(aliases = {"blackjack", "21", "bj"}, description = "BlackJack 21", async = true)
    public String onBlackJackCommand(User user, TextChannel textChannel, String[] arg, DiscordApi api) {
        int amounts;
        Cards deck = new Cards();
        ArrayList<Player> players = new ArrayList<>();

        Player bank;
        ExtendedBoolean splitted = new ExtendedBoolean(false);
        StringBuilder options = new StringBuilder();

        if(arg.length > 1) {
            System.out.println(user.getName() + ": Blackjack: arg lenght not 1, value: " + arg.length);

            return showHelp();
        }
        try {
            amounts = Integer.parseInt(arg[0]);
        } catch (Exception e) {
            amounts = 1;
        }
        final int amount = amounts;
        if (amount <= 0) {
            System.out.println(user.getName() + ": Negative amount");
            return "Try with a positive amount";
        } else if (amount > Money.getMoney(user)) {
            System.out.println(user.getName() + ": Blackjack: not enough money");
            Money.setMoney(user, 0, 0);
            return "Not enough money, try the **daily** command";
        }

        if(using.contains(user))
            return "";
        else
            using.add(user);

        bank = new Player("Bank");

        players.add(new Player(user.getName()));
        Money.removeMoney(user, amount);

        //Draw one card each
        players.get(0).add(deck.draw());
        bank.add(deck.draw());
        players.get(0).add(deck.draw());

        options.append("**`Hit`** **`Stand`**");

        //Check for split
        if(players.get(0).sameCardsInHand()) {
            options.append(" **`Split`**");
        }

        textChannel.sendMessage(players.get(0).addEmbed(bank.addEmbed(getEmbed())));
        textChannel.sendMessage(options.toString());

        final long timestamp = System.currentTimeMillis();

        ExtendedBoolean stand = new ExtendedBoolean(false);

        AtomicReference<ListenerManager> lm = new AtomicReference<>();
        lm.set(textChannel.addMessageCreateListener(e -> {
            if (e.getMessage().getUserAuthor().get().getId() == user.getId()) {
                if (e.getMessage().getContent().equalsIgnoreCase("hit")) {
                    players.get(0).add(deck.draw());

                    if (players.get(0).getTotalValue() > 21) {
                        stand.set(true);
                        lm.get().remove();
                    }
                    if (stand.isNot()) {
                        EmbedBuilder embed = bank.addEmbed(getEmbed());
                        for (Player ply : players) {
                            embed = ply.addEmbed(embed);
                        }
                        textChannel.sendMessage(embed);
                        textChannel.sendMessage("**`Hit`** **`Stand`**");
                    }

                } else if (e.getMessage().getContent().equalsIgnoreCase("stand")) {
                    stand.set(true);
                    lm.get().remove();
                } else if (splitted.isNot() && e.getMessage().getContent().equalsIgnoreCase("split")) {
                    Money.removeMoney(user, amount);
                    players.add(players.get(0).split());
                    for (Player ply : players)
                        ply.add(deck.draw());
                    EmbedBuilder embed = bank.addEmbed(getEmbed());
                    for (Player ply : players) {
                        embed = ply.addEmbed(embed);
                    }
                    textChannel.sendMessage(embed);
                    textChannel.sendMessage("**`Hit`** **`Stand`**");
                    splitted.set(true);
                }
            }
        }).removeAfter(INTERVAL, TimeUnit.MILLISECONDS));
        lm.get().addRemoveHandler(() -> stand.set(true));
        while (stand.isNot()) {
            Thread.onSpinWait();
        }

        lm.get().remove();

        if(splitted.is()) {
            EmbedBuilder embed = bank.addEmbed(getEmbed());
            for (Player ply : players) {
                embed = ply.addEmbed(embed);
            }
            textChannel.sendMessage(embed);
            textChannel.sendMessage("**`Hit`** **`Stand`**");

            ExtendedBoolean stand1 = new ExtendedBoolean(false);

            AtomicReference<ListenerManager> lm2 = new AtomicReference<>();

            lm2.set(textChannel.addMessageCreateListener(e -> {
                if (e.getMessage().getUserAuthor().get().getId() == user.getId()) {
                    if (e.getMessage().getContent().equalsIgnoreCase("hit")) {
                        players.get(1).add(deck.draw());

                        if (players.get(1).getTotalValue() > 21) {
                            stand1.set(true);
                            lm2.get().remove();
                        }
                        if (stand1.isNot()) {
                            EmbedBuilder embed1 = bank.addEmbed(getEmbed());
                            for (Player ply : players) {
                                embed1 = ply.addEmbed(embed1);
                            }
                            textChannel.sendMessage(embed1);
                            textChannel.sendMessage("**`Hit`** **`Stand`**");
                        }

                    } else if (e.getMessage().getContent().equalsIgnoreCase("stand")) {
                        stand1.set(true);
                        lm2.get().remove();
                    }
                }
            }).removeAfter(INTERVAL, TimeUnit.MILLISECONDS));
            lm2.get().addRemoveHandler(() -> stand1.set(true));

            while (stand1.isNot()) {
                Thread.onSpinWait();
            }

            lm2.get().remove();

        }

        bank.add(deck.draw());

        while (bank.getTotalValue() <= 16) {
            if(bank.getTotalValue() > players.get(0).getTotalValue() || players.get(0).getTotalValue() > 21)
                break;
            bank.add(deck.draw());
        }

        EmbedBuilder embed = bank.addEmbed(getEmbed());
        for (Player ply : players) {
            embed = ply.addEmbed(embed);
        }
        textChannel.sendMessage(embed);


        //Calculate win/winner
        int bankGame = bank.getTotalValue();
        int totalWin = 0;
        StringBuilder winner = new StringBuilder();


        for(Player ply : players) {
            int playerGame = ply.getTotalValue();

            winner.append(ply.getName());
            winner.append(" game: ");
            if (playerGame > 21) {
                winner.append("You lose!");
            } else if((playerGame == 21) && ((bankGame < 21) || (bankGame > 21)) && splitted.isNot() && (ply.getHandCards() == 2)) {
                winner.append("You win! [BlackJack]");
                totalWin += amount * 3;
            } else if (playerGame == bankGame) {
                if (playerGame == 21 && ply.getHandCards() == 2 && splitted.isNot()) {
                    if (bank.getHandCards() == 2) {
                        winner.append("Draw!");
                        totalWin += amount;
                    }
                    else {
                        winner.append("You win! [BlackJack]");
                        totalWin += amount * 3;
                    }
                } else {
                    winner.append("Draw!");
                    totalWin += amount;
                }
            } else if (bankGame > 21) {
                winner.append("You win!");
                totalWin += amount * 2;
            } else if (bankGame > playerGame) {
                winner.append("You lose!");
            } else {
                winner.append("You win!");
                totalWin += amount * 2;
            }
            winner.append("\n");
        }
        winner.append("Total win: **");
        winner.append(totalWin);
        winner.append("$**");
        Money.addMoney(user, totalWin);
        using.remove(user);
        return winner.toString();
    }

    private String showHelp() {
        return "**Usage:**\n```Rb.blackjack [amount]```";
    }


    private class Player {
        private ArrayList<Cards.Card> hand;
        private String name;

        Player() {
            hand = new ArrayList<>();
        }

        Player(String username) {
            this();
            this.name = username;
        }

        void add(Cards.Card card) {
            this.hand.add(card);
        }

        Player split() {
            Player player = new Player(this.getName() + "'s split");
            player.add(this.hand.remove(0));
            return player;
        }

        int getTotalValue() {
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

        int getHandCards() {
            return this.hand.size();
        }

        boolean sameCardsInHand() {
            if(this.getHandCards() == 2)
                return this.hand.get(0).getValue() == this.hand.get(1).getValue();
            return false;
        }


        String getName() {
            return name;
        }

        synchronized EmbedBuilder addEmbed(EmbedBuilder embedBuilder) {
            return embedBuilder.addField(this.getName() + "'s hand", this.toString() + "\n").addInlineField("Total: " + String.valueOf(this.getTotalValue()), "\u200b");
        }

        @Override
        public synchronized String toString(){
            StringBuilder string = new StringBuilder();

            for(Cards.Card card : hand) {
                string.append(card.toString());
                string.append("\n");
            }
            return string.toString();
        }

    }

}
