package com.github.lrhel.rabbot;

import com.github.lrhel.rabbot.sqlite.Sqlite;
import org.javacord.api.entity.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Money {
    public static void setMoney(User user, int amount, int timestamp) {
        Connection c = Sqlite.getInstance().getConnection();
        String sql = "SELECT * FROM money WHERE user_id = ?";
        try {
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, user.getIdAsString());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                sql = "UPDATE money SET money = ?, timestamp = ? WHERE user_id = ?";
            }
            else {
                sql = "INSERT INTO money(money, timestamp, user_id) VALUES(?, ?, ?)";
            }
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, amount);
            pstmt.setInt(2, timestamp);
            pstmt.setString(3, user.getIdAsString());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setMoney(User user, int amount) {
        Money.setMoney(user, amount, 0);
    }

    public static int getMoney(User user) {
        Connection c = Sqlite.getInstance().getConnection();
        String sql = "SELECT * FROM money WHERE user_id = ?";
        try {
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, user.getIdAsString());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                return rs.getInt("money");
            }
            else
                return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getTimestamp(User user) {
        Connection c = Sqlite.getInstance().getConnection();
        String sql = "SELECT * FROM money WHERE user_id = ?";
        try {
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, user.getIdAsString());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                return rs.getInt("timestamp");
            }
            else
                return 0;
        } catch (Exception e) {
            return 0;
        }
    }
}
