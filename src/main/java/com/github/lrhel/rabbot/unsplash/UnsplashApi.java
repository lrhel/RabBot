package com.github.lrhel.rabbot.unsplash;

import com.github.lrhel.rabbot.unsplash.entity.UnsplashImage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UnsplashApi {
    private final StringBuilder clientId;
    private static final StringBuilder URL_BASE = new StringBuilder("https://api.unsplash.com/");

    public UnsplashApi(String client_id) {
        this.clientId = new StringBuilder("client_id=").append(client_id);
    }

    public UnsplashImage getRandomImage() throws Exception {
        return getRandomImage("");
    }

    public UnsplashImage getRandomImage(String query) throws Exception {
        JSONParser parser = new JSONParser();
        StringBuilder url = new StringBuilder(URL_BASE);
        url.append("photos/random");
        url.append("?query=");
        url.append(query);
        url.append("&");
        url.append(this.clientId);
        JSONObject image = (JSONObject) parser.parse(new BufferedReader(new InputStreamReader(new URL(url.toString()).openStream())));
        return UnsplashImage.parse(image);

    }
}
