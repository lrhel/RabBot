package com.github.lrhel.rabbot.unsplash.entity;

import org.json.simple.JSONObject;

public class UnsplashUser {
    private String id;

    private String username;

    private String name;

    private String portfolio_url;

    public UnsplashUser(String id, String username, String name, String portfolio_url) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.portfolio_url = portfolio_url;
    }

    public static UnsplashUser parse(JSONObject obj) {
        String id = (String) obj.get("id");
        String username = (String) obj.get("username");
        String name = (String) obj.get("name");
        String portfolio_url = (String) obj.get("portfolio_url");

        return new UnsplashUser(id, username, name, portfolio_url);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPortfolio_url() {
        return portfolio_url;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(this.getId()).append("\n");
        sb.append("Username: ").append(this.getUsername()).append("\n");
        sb.append("Name: ").append(this.getName()).append("\n");
        sb.append("Portfolio URL: ").append(this.getPortfolio_url()).append("\n");

        return sb.toString();
    }
}
