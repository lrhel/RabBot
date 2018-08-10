package com.github.lrhel.rabbot.dblApi.entity;

import org.json.simple.JSONObject;

public class DblUser {
    String username;
    String discriminator;
    String id;
    String avatar;

    public DblUser(String username, String discriminator, String id, String avatar) {
        this.username = username;
        this.discriminator = discriminator;
        this.id = id;
        this.avatar = avatar;
    }

    public static DblUser parse(JSONObject obj) {
        String username = (String) obj.get("username");
        String discriminator = (String) obj.get("discriminator");
        String id = (String) obj.get("id");
        String avatar = (String) obj.get("avatar");
        return new DblUser(username, discriminator, id, avatar);
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public String getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUsername() {
        return username;
    }
}
