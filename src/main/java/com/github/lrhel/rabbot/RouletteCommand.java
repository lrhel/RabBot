package com.github.lrhel.rabbot;

import com.github.lrhel.rabbot.sqlite.Sqlite;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RouletteCommand implements CommandExecutor {
    @Command(aliases = {"roulette"}, description = "Bet money on the Roulette")
    public String rouletteCommand(User user, String[] param){
        Connection c = Sqlite.getInstance().getConnection();
        Roulette roulette = Roulette.getInstance();
        int amount;
        int win;

        String sql = "SELECT * FROM money WHERE user_id = ?";

        if(param.length != 2){
            return "Usage: ```rb.roulette [amount] [0-36 | RED/BLACK | ODD/EVEN]```";
        } else {
            try {
                amount = Integer.parseInt(param[0]);
            } catch (NumberFormatException e) {
                return "Not a valid bet";
            }
        }
        try {
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, user.getIdAsString());
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                int money = rs.getInt("money");
                if(amount <= 0)
                    return "Not a valid bet";
                if(money < amount)
                    return "Sorry, not enough money!";
                String spin = roulette.spin();
                if(param[1].equalsIgnoreCase(spin.split(" ")[0])){
                    win = 35 * amount;
                }
                else if(param[1].equalsIgnoreCase(spin.split(" ")[1]) || (!spin.split(" ")[0].equalsIgnoreCase("0") && param[1].equalsIgnoreCase(spin.split(" ")[2]))){
                    win = amount;
                }
                else {
                    sql = "UPDATE money SET money = ? WHERE user_id = ?";
                    pstmt = c.prepareStatement(sql);
                    pstmt.setInt(1, money - amount);
                    pstmt.setString(2, user.getIdAsString());
                    pstmt.executeUpdate();
                    return spin + "\nSorry, you loose";
                }

                sql = "UPDATE money SET money = ? WHERE user_id = ?";
                pstmt = c.prepareStatement(sql);
                pstmt.setInt(1, win + money);
                pstmt.setString(2, user.getIdAsString());
                pstmt.executeUpdate();
                return spin + "\nYeaaahhh, you win " + (win + amount);


            } else {
                return "Sorry, not enough money!";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
