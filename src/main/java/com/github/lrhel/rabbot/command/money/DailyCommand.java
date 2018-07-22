package com.github.lrhel.rabbot.command.money;

import com.github.lrhel.rabbot.sqlite.Sqlite;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class DailyCommand implements CommandExecutor {
    private static final int INTERVAL =  60 * 60 * 1000;
    //

    @Command(aliases = {"daily"}, description = "Daily money!")
    public String dailyCommand(User user, Server server){
        Connection c = Sqlite.getInstance().getConnection();
        String sql = "SELECT * FROM money WHERE user_id = ?";
        Random rng = new Random(System.currentTimeMillis());
        int money = 0;

        if(rng.nextInt(100) < 20)
            money += 400;
        else if(rng.nextInt(100) < 50)
            money += 200;
        else
            money += 100;
        money += rng.nextInt(150);

        try {
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, user.getIdAsString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if ((rs.getInt("timestamp") + INTERVAL) > (System.currentTimeMillis() % Integer.MAX_VALUE)) {
                    int seconde = ((rs.getInt("timestamp") - Math.toIntExact(System.currentTimeMillis() % Integer.MAX_VALUE) + INTERVAL) / (1000));
                    return "Try in " + (seconde / 60) + " minutes and " + (seconde % 60) + " seconds";
                } else {
                    money += rs.getInt("money");
                    sql = "UPDATE money SET money = ?, timestamp = ? WHERE user_id = ?";
                    pstmt = c.prepareStatement(sql);
                    pstmt.setInt(1, money);
                    pstmt.setInt(2, (int) (System.currentTimeMillis() % Integer.MAX_VALUE));
                    pstmt.setString(3, user.getIdAsString());
                    pstmt.executeUpdate();
                    return "Yeahhh **" + user.getName() + "**, you got **" + (money - rs.getInt("money")) + "$**";
                }

            } else {
                sql = "INSERT INTO money(user_id, money, timestamp) VALUES(?,?,?)";
                pstmt = c.prepareStatement(sql);
                pstmt.setString(1, user.getIdAsString());
                pstmt.setInt(2, money);
                pstmt.setInt(3, (int) (System.currentTimeMillis() % Integer.MAX_VALUE));
                pstmt.executeUpdate();
                return "Yeahhh **" + user.getName() + "**, you got **" + money + "$**";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
