package com.github.lrhel.rabbot.command.money;

import com.github.lrhel.rabbot.sqlite.Sqlite;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GiveMoneyCommand implements CommandExecutor {
    @Command(aliases = {"givemoney"}, showInHelpPage = false)
    public String giveMoneyCommand(User user, Server server, Message message, String[] arg){
        if(!user.isBotOwner())
            return "";

        ArrayList<User> userList = new ArrayList<>(message.getMentionedUsers());
        if(userList.isEmpty())
            return "";
        try {
            Connection c = Sqlite.getInstance().getConnection();
            int amount = Integer.parseInt(arg[0]);
            int money = 0;

            String sql = "SELECT * FROM money WHERE user_id = ?";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, userList.get(0).getIdAsString());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                money = rs.getInt("money");
                sql = "UPDATE money SET money = ? WHERE user_id = ?";
                pstmt = c.prepareStatement(sql);
                pstmt.setInt(1, amount + money);
                pstmt.setString(2, userList.get(0).getIdAsString());
                pstmt.executeUpdate();

            }
            else {
                sql = "INSERT INTO money(user_id, money, timestamp) VALUES(?,?,?)";
                pstmt = c.prepareStatement(sql);
                pstmt.setInt(1, amount + money);
                pstmt.setString(2, userList.get(0).getIdAsString());
                pstmt.setInt(3, 0);
                pstmt.executeUpdate();
            }
            return userList.get(0).getDisplayName(server) + " got **" + amount + "$**";

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
