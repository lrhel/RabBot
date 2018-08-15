package com.github.lrhel.rabbot.command.games;

import com.github.lrhel.rabbot.Roulette;
import com.github.lrhel.rabbot.sqlite.Sqlite;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

public class RouletteCommand implements CommandExecutor {
    @Command(aliases = {"roulette"}, description = "Bet money on the Roulette")
    public String rouletteCommand(User user, Server server, String[] param, TextChannel textChannel){
        if (user.isBot()) { return ""; }

        Connection c = Sqlite.getInstance().getConnection();
        Roulette roulette = Roulette.getInstance();
        int amount;
        int win;

        String sql = "SELECT * FROM money WHERE user_id = ?";

        if(param.length > 2 || param.length == 0){
            return showHelp();
        } else {
            try {
                amount = param.length == 1 ? 1 : Integer.parseInt(param[0]);
            } catch (NumberFormatException nfe) {
                Message lm = textChannel.sendMessage("Sorry **" + user.getDisplayName(server) +  "**, not a valid bet!")
                        .join();
                lm.addMessageEditListener(e -> {}).removeAfter(10, TimeUnit.SECONDS)
                        .addRemoveHandler(() -> lm.delete().join());
                return "";
            }
        }

        if(param.length == 1){
            try {
                if(Integer.parseInt(param[0]) > 36)
                    return showHelp();
            } catch (Exception e) {
                if (!param[0].equalsIgnoreCase("black") && !param[0].equalsIgnoreCase("red")
                        && !param[0].equalsIgnoreCase("even") && !param[0].equalsIgnoreCase("odd"))
                    return showHelp();
            }
        }
        else {
            try {
                int value = Integer.parseInt(param[1]);
                if(value < 0 || value > 36)
                    return showHelp();
            } catch(Exception e) {
                if (!param[1].equalsIgnoreCase("black") && !param[1].equalsIgnoreCase("red")
                        && !param[1].equalsIgnoreCase("even") && !param[1].equalsIgnoreCase("odd"))
                    return showHelp();
            }
        }
        try {
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, user.getIdAsString());
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                int money = rs.getInt("money");
                if(amount <= 0) {
                    Message lm = textChannel.sendMessage("Sorry **" + user.getDisplayName(server) +  "**, not a valid bet!")
                            .join();
                    lm.addMessageEditListener(e -> {}).removeAfter(10, TimeUnit.SECONDS)
                            .addRemoveHandler(() -> lm.delete().join());
                    return "";
                }
                if(money < amount){
                    Message lm = textChannel.sendMessage("Sorry **" + user.getDisplayName(server) +  "**, not enough money!")
                            .join();
                    lm.addMessageEditListener(e -> {}).removeAfter(10, TimeUnit.SECONDS)
                            .addRemoveHandler(() -> lm.delete().join());
                    return "";
                }
                String spin = roulette.spin();

                if(param.length == 2 && param[1].equalsIgnoreCase(spin.split(" ")[0])){
                    win = 35 * amount;
                }
                else if(param.length == 2 && (param[1].equalsIgnoreCase(spin.split(" ")[1]) || (!spin.split(" ")[0].equalsIgnoreCase("0") && param[1].equalsIgnoreCase(spin.split(" ")[2])))){
                    win = amount;
                }

                else if(param.length == 1 && param[0].equalsIgnoreCase(spin.split(" ")[0])){
                    win = 35 * amount;
                }
                else if(param.length == 1 && (param[0].equalsIgnoreCase(spin.split(" ")[1]) || (!spin.split(" ")[0].equalsIgnoreCase("0") && param[0].equalsIgnoreCase(spin.split(" ")[2])))){
                    win = amount;
                }
                else {
                    sql = "UPDATE money SET money = ? WHERE user_id = ?";
                    pstmt = c.prepareStatement(sql);
                    pstmt.setInt(1, money - amount);
                    pstmt.setString(2, user.getIdAsString());
                    pstmt.executeUpdate();
                    return "**" + spin + "**" + "\nSorry **" + user.getDisplayName(server) + "**, you lose";
                }

                sql = "UPDATE money SET money = ? WHERE user_id = ?";
                pstmt = c.prepareStatement(sql);
                pstmt.setInt(1, win + money);
                pstmt.setString(2, user.getIdAsString());
                pstmt.executeUpdate();
                return "**" + spin + "**\nYeaaahhh **" + user.getDisplayName(server) + "**, you win **" + (win + amount) + "$**";


            } else {
                Message lm = textChannel.sendMessage("Sorry **" + user.getDisplayName(server) +  "**, not enough money!")
                        .join();
                lm.addMessageEditListener(e -> {}).removeAfter(5, TimeUnit.SECONDS)
                        .addRemoveHandler(() -> lm.delete().join());
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String showHelp() {
        return "**Usage:**\n```rb.roulette [amount] [0-36 | RED/BLACK | ODD/EVEN]```";
    }
}
