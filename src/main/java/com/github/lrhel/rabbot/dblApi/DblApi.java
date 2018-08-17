package com.github.lrhel.rabbot.dblApi;

import com.github.lrhel.rabbot.dblApi.entity.DblUser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class DblApi {
    private static final StringBuilder URL_BASE = new StringBuilder("https://discordbots.org/api/");
    private String botID;

    public DblApi() {

    }

    public DblApi setId(String id) {
        this.botID = id;
        return this;
    }

    public long getPoints() throws Exception {
        JSONParser parser = new JSONParser();
        StringBuilder url = new StringBuilder(URL_BASE);
        url.append("bots/").append(this.botID);
        JSONObject obj = (JSONObject) parser.parse(new BufferedReader(new InputStreamReader(new URL(url.toString()).openStream())));
        return (Long) obj.get("points");
    }

    public long getMonthlyPoints() throws Exception {
        JSONParser parser = new JSONParser();
        StringBuilder url = new StringBuilder(URL_BASE);
        url.append("bots/").append(this.botID);
        JSONObject obj = (JSONObject) parser.parse(new BufferedReader(new InputStreamReader(new URL(url.toString()).openStream())));

        return (Long) obj.get("monthlyPoints");
    }



    public ArrayList<DblUser> getVotes() throws Exception {
        JSONParser parser = new JSONParser();
        StringBuilder url = new StringBuilder(URL_BASE);
        ArrayList<DblUser> list = new ArrayList<>();
        url.append("bots/").append(this.botID).append("/votes");
        JSONArray array = (JSONArray) parser.parse(new BufferedReader(new InputStreamReader(new URL(url.toString()).openStream())));
        Iterator iterator = array.iterator();
        while (iterator.hasNext()) {
            JSONObject obj = (JSONObject) iterator.next();
            list.add(DblUser.parse(obj));
        }
        return list;
    }

    public boolean hasVoted(String id) throws Exception {
        JSONParser parser = new JSONParser();
        StringBuilder url = new StringBuilder(URL_BASE);
        url.append("bots/").append(this.botID).append("/check?userId=").append(id);
        JSONObject obj = (JSONObject) parser.parse(new BufferedReader(new InputStreamReader(new URL(url.toString()).openStream())));
        if((Integer) obj.get("voted") == 1) {
            return true;
        }
        else {
            return false;
        }
    }

}
