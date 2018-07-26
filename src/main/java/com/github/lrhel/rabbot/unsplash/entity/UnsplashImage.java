package com.github.lrhel.rabbot.unsplash.entity;

import org.json.simple.JSONObject;

import java.awt.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class UnsplashImage {
    private String id;

    private Instant creationTime;

    private int width;

    private int height;

    private Color color;

    private String description;

    private UnsplashUser author;

    private HashMap<String, String> urls;

    private UnsplashImage(String id, String creationTime, int width, int height, String color, String description, UnsplashUser user, HashMap<String, String> urls){

        this.id = id;

        this.width = width;

        this.height = height;

        color = color.replace("#", "");

        try {
            this.color = new Color(Integer.parseInt(color));
        } catch (Exception e) {
            this.color = Color.GRAY;
        }

        this.description = description;

        this.author = user;

        this.urls = urls;
    }

    public static UnsplashImage parse(JSONObject obj) {
        String id = (String) obj.get("id");
        String creationTime = (String) obj.get("created_at");
        int width = ((Long) obj.get("width")).intValue();
        int height = ((Long) obj.get("height")).intValue();
        String color = (String) obj.get("color");
        String desc = (String) obj.get("description");
        JSONObject author = (JSONObject) obj.get("user");
        HashMap<String, String> urls = new HashMap<>((Map<String, String>) obj.get("urls"));

        return new UnsplashImage(id, creationTime, width, height, color, desc, UnsplashUser.parse(author), urls);

    }

    public String getId() {
        return id;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public UnsplashUser getAuthor() {
        return author;
    }

    public HashMap<String, String> getUrls() {
        return urls;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ID: ").append(this.getId()).append("\n");
        sb.append("Creation Time: ").append(this.getCreationTime()).append("\n");
        sb.append("Width: ").append(this.getWidth()).append("\n");
        sb.append("Height: ").append(this.getHeight()).append("\n");
        sb.append("Color: ").append(this.getColor()).append("\n");
        sb.append("Description: ").append(this.getDescription()).append("\n");
        sb.append("Author: ").append(this.getAuthor()).append("\n");
        sb.append("URLs: ");
        this.getUrls().forEach((k, v) -> sb.append(v).append("\n"));

        return sb.toString();
    }
}
