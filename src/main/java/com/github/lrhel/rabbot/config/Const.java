package com.github.lrhel.rabbot.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static java.lang.Long.parseLong;

public final class Const {
    public static final String TOKEN;
    public static final String PW_TOKEN;
    public static final String BFD_TOKEN;
    public static final String MASHAPE_TOKEN;
    public static final String CHALLONGE_TOKEN;
    public static final String UNSPLASH_TOKEN;
    public static final String DISCORDLIST_TOKEN;
    public static final long SMATH_ID;
    public static final long THUGA_ID;
    public static final long BOT_ID;

    static {

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File("const.properties")));

            TOKEN = properties.getProperty("token");
            PW_TOKEN = properties.getProperty("pw-token");
            BFD_TOKEN = properties.getProperty("bfd-token");
            MASHAPE_TOKEN = properties.getProperty("mashape-token");
            CHALLONGE_TOKEN = properties.getProperty("challonge-token");
            UNSPLASH_TOKEN = properties.getProperty("unsplash-token");
            DISCORDLIST_TOKEN = properties.getProperty("discordlist-token");
            SMATH_ID = parseLong(properties.getProperty("smath-id"));
            THUGA_ID = parseLong(properties.getProperty("thuga-id"));
            BOT_ID = parseLong(properties.getProperty("bot-id"));
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load file const.properties", e);
        }
    }

    private Const() {
    }
}