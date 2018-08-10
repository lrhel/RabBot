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
}
