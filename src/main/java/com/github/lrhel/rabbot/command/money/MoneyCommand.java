package com.github.lrhel.rabbot.command.money;

import com.github.lrhel.rabbot.Money;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

import java.util.concurrent.TimeUnit;

public class MoneyCommand implements CommandExecutor {
    @Command(aliases = {"money", "cash"}, description = "Show your current money")
    public String MoneyCommand(User user, TextChannel textChannel) {
        int money = Money.getMoney(user);
        if (money > 0) {
            return "You have " + money + "$";
        }
        else {
            Message message = textChannel.sendMessage("You have 0$... Try the **daily** command").join();
            message.addMessageEditListener(e -> {}).removeAfter(5, TimeUnit.SECONDS)
                    .addRemoveHandler(() -> message.delete());
            return "";
        }
    }
}