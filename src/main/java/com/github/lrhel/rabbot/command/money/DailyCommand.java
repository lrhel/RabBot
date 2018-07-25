package com.github.lrhel.rabbot.command.money;

import com.github.lrhel.rabbot.Money;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.user.User;
import java.util.Random;

public class DailyCommand implements CommandExecutor {
    private static final int INTERVAL =  60 * 60 * 1000;

    @Command(aliases = {"daily"}, description = "Daily money!")
    public String onDailyCommand(User user){
        Random rng = new Random(System.currentTimeMillis());
        int totalMoney = Money.getMoney(user);
        int timestamp = Money.getTimestamp(user);
        int money = 0;

        if(rng.nextInt(100) < 20)
            money += 4000;
        else if(rng.nextInt(100) < 50)
            money += 2000;
        else
            money += 1000;
        money += rng.nextInt(1500);

        if ((timestamp + INTERVAL) > (System.currentTimeMillis() % Integer.MAX_VALUE)) {
            int seconde = ((timestamp - (Math.toIntExact(System.currentTimeMillis() % Integer.MAX_VALUE)) + INTERVAL) / (1000));
            return "Try in " + (seconde / 60) + " minutes and " + (seconde % 60) + " seconds";
        } else {
            totalMoney += money;
            Money.setMoney(user, totalMoney, (int) (System.currentTimeMillis() % Integer.MAX_VALUE));
            return "Yeahhh **" + user.getName() + "**, you got **" + money + "$**";
        }
    }
}
