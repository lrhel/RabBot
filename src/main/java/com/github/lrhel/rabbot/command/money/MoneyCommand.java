package com.github.lrhel.rabbot.command.money;

import com.github.lrhel.rabbot.sqlite.Sqlite;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MoneyCommand implements CommandExecutor {
    @Command(aliases = {"money", "cash"}, description = "Show your current money")
    public String MoneyCommand(User user){
        Connection c = Sqlite.getInstance().getConnection();
        String sql = "SELECT * FROM money WHERE user_id = ?";
        try {
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, user.getIdAsString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return "You have " + rs.getInt("money") + "$";
            } else {
                return "You have 0$... Try the *daily* command";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
