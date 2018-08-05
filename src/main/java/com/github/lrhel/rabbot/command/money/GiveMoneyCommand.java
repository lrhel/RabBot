package com.github.lrhel.rabbot.command.money;

import com.github.lrhel.rabbot.Money;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

public class GiveMoneyCommand implements CommandExecutor {
    @Command(aliases = {"givemoney"}, showInHelpPage = false)
    public String giveMoneyCommand(User user, Message message, Object[] args) {

        Number amount;

        ArrayList<User> userList = new ArrayList<>(message.getMentionedUsers());
        StringBuilder returnMessage = new StringBuilder();

        Optional optional = Stream.of(args).filter(Number.class::isInstance).findAny();

        if (userList.isEmpty() || !user.isBotOwner() || !optional.isPresent()) {
            return "";
        }

        amount = (Number) optional.get();

        for (User users : userList) {
            Money.addMoney(users, amount.intValue());
            returnMessage.append(users.getName()).append(" ");
        }

        return returnMessage.append("got **").append(amount).append("$**").toString();
    }
}