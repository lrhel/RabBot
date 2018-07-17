package com.github.lrhel.rabbot.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;

public class Sqlite {

    Connection c;

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
}
