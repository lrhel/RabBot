package com.github.lrhel.rabbot.sqlite;

import org.javacord.api.entity.user.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Sqlite {

    private Connection c;

    private Sqlite(){
        try {
            Class.forName("org.sqlite.JDBC");
            this.c = DriverManager.getConnection("jdbc:sqlite:pokebot.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
            System.exit(0);
        }
    }

    private static class SqliteHolder {
        private final static Sqlite instance = new Sqlite();
    }

    public static Sqlite getInstance(){
        return SqliteHolder.instance;
    }

    public Connection getConnection(){
        return this.c;
    }

    public static int getTimestampFromSql(User user, String sql) {
        try {
            PreparedStatement pstmt = getInstance().getConnection().prepareStatement(sql);
            pstmt.setString(1, user.getIdAsString());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                return rs.getInt("timestamp");
            }
            else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getBonusTimestampFromSql(User user, String sql) {
        try {
            PreparedStatement pstmt = getInstance().getConnection().prepareStatement(sql);
            pstmt.setString(1, user.getIdAsString());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                return rs.getInt("bonus_timestamp");
            }
            else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean resetCatchTimestamp(User user) {
        String sql = "UPDATE catch SET timestamp = 0 WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, user.getIdAsString());
            return !preparedStatement.execute();
        } catch (Exception ignored) { }
        return false;
    }

    public static boolean resetCatchTimestamp() {
        String sql = "UPDATE catch SET timestamp = 0";
        try {
            PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement(sql);
            return !preparedStatement.execute();
        } catch (Exception ignored) { }
        return false;
    }

    public static boolean resetDailyTimestamp(User user) {
        String sql = "UPDATE money SET timestamp = 0 WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, user.getIdAsString());
            return !preparedStatement.execute();
        } catch (Exception ignored) { }
        return false;
    }

    public static boolean resetDailyTimestamp() {
        String sql = "UPDATE money SET timestamp = 0";
        try {
            PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement(sql);
            return !preparedStatement.execute();
        } catch (Exception ignored) { }
        return false;
    }

    public static boolean resetBonusTimestamp(User user) {
        String sql = "UPDATE money SET bonus_timestamp = 0 WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, user.getIdAsString());
            return !preparedStatement.execute();
        } catch (Exception ignored) { }
        return false;
    }

    public static boolean resetBonusTimestamp() {
        String sql = "UPDATE money SET bonus_timestamp = 0";
        try {
            PreparedStatement preparedStatement = getInstance().getConnection().prepareStatement(sql);
            return !preparedStatement.execute();
        } catch (Exception ignored) { }
        return false;
    }
}
