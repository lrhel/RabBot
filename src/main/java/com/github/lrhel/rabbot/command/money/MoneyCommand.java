package com.github.lrhel.rabbot.command.money;

import java.util.concurrent.TimeUnit;

import de.kaleidox.javacord.util.commands.Command;

import com.github.lrhel.rabbot.Money;
import com.github.lrhel.rabbot.utility.Utility;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;

public class MoneyCommand {
    @Command(aliases = {"money", "cash"}, description = "Show your current money")
    public String onMoneyCommand(User user, TextChannel textChannel) {
        if (user.isBot()) {
            return "";
        }

        int money = Money.getMoney(user);
        if (money > 0) {
            return "You have **" + money + "$**";
        } else {
            Money.setMoney(user, 0, 0);
            textChannel.sendMessage("You have 0$... Try the **daily** command").thenAccept(Utility.getMessageDeleter(5, TimeUnit.SECONDS)).join();
            return "";
        }
    }
}