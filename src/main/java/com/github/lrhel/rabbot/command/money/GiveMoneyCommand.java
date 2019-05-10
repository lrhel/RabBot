package com.github.lrhel.rabbot.command.money;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import de.kaleidox.javacord.util.commands.Command;

import com.github.lrhel.rabbot.Money;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

public class GiveMoneyCommand {
    @Command(shownInHelpCommand = false)
    public String givemoney(User user, Message message, Object[] args) {
        if (user.isBot()) {
            return "";
        }


        Number amount;

        ArrayList<User> userList = new ArrayList<>(message.getMentionedUsers());
        StringBuilder returnMessage = new StringBuilder();

        Optional optional = Stream.of(args).filter(Number.class::isInstance).findAny();

        if (userList.isEmpty() || !optional.isPresent()) {
            return "";
        }

        amount = (Number) optional.get();

        if (user.isBotOwner()) {

            for (User users : userList) {
                Money.addMoney(users, amount.intValue());
                returnMessage.append(users.getName()).append(" ");
            }

            return returnMessage.append("got **").append(amount).append("$**").toString();

        } else {
            if (Money.getMoney(user) < amount.intValue() * userList.size()) {
                return "Not enough money";
            } else {
                if (amount.intValue() < 1) {
                    return "Give a real amount";
                }
                for (User users : userList) {
                    Money.addMoney(users, amount.intValue());
                    Money.removeMoney(user, amount.intValue());
                    returnMessage.append(users.getName())
                            .append(" got **")
                            .append(amount).append("$**\n");
                }

                return returnMessage.toString();
            }
        }

    }
}