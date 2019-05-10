package com.github.lrhel.rabbot.command.money;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import de.kaleidox.javacord.util.commands.Command;

import com.github.lrhel.rabbot.Money;
import com.github.lrhel.rabbot.utility.Utility;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;

public class DailyCommand {
    private static final int INTERVAL = 60 * 60 * 1000;

    @Command(description = "Daily money!")
    public void daily(User user, TextChannel textChannel) {
        if (user.isBot()) {
            return;
        }

        Random rng = new Random(System.currentTimeMillis());
        int totalMoney = Money.getMoney(user);
        int timestamp = Money.getTimestamp(user);
        int now = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
        int money = 0;

        if (rng.nextInt(100) < 20) {
            money += 4000;
        } else if (rng.nextInt(100) < 50) {
            money += 2000;
        } else {
            money += 1000;
        }

        money += rng.nextInt(1500);

        if ((timestamp + INTERVAL) > now && (timestamp - now + INTERVAL) <= INTERVAL) {
            int second = (timestamp - now + INTERVAL) / 1000;
            textChannel.sendMessage("Try in " + (second / 60) + " minutes and " + (second % 60) + " seconds").thenAccept(Utility.getMessageDeleter(5, TimeUnit.SECONDS));
        } else {
            totalMoney += money;
            Money.setMoney(user, totalMoney, now);
            textChannel.sendMessage("Yeahhh **" + user.getName() + "**, you got **" + money + "$**");
        }
    }
}