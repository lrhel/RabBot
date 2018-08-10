package com.github.lrhel.rabbot;

import com.github.lrhel.rabbot.sqlite.Sqlite;
import org.javacord.api.entity.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.github.lrhel.rabbot.sqlite.Sqlite.getTimestampFromSql;

public interface Money {

    /**
     * Set the user total amount to the given amount
     * @param user the user to set the amount
     * @param amount the amount to set
     * @param timestamp of the last time daily command was used
     */
     static void setMoney(User user, int amount, int timestamp) {
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

    static void addBonus(User user, int amount) {

    }

    static void setMoney(User user, int amount) {
        Money.setMoney(user, amount, getTimestamp(user));
    }

    /**
     * Add the given amount to the total money of the given user
     * @param user the user to add the money
     * @param amount the amount to add
     */
    static void addMoney(User user, int amount) {
        setMoney(user, getMoney(user) + amount);
    }

    /**
     * Remove the given amount to the total money of the given user
     * @param user the user to remove his money
     * @param amount the amount to remove
     */
    static void removeMoney(User user, int amount) {
        addMoney(user, -amount);
    }

    /**
     * Getters for the Money column's of the database
     * @param user the user to get his money
     * @return the money of the user
     */
     static int getMoney(User user) {
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

    /**
     * Get the timestamp of the last time the given user used the daily command
     * @param user the user to get the last timestamp of the daily command
     * @return the timestamp
     */
    static int getTimestamp(User user) {
        String sql = "SELECT * FROM money WHERE user_id = ?";
        return getTimestampFromSql(user, sql);
    }
}
