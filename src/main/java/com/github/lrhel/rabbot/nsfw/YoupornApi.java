package com.github.lrhel.rabbot.nsfw;

import com.github.lrhel.rabbot.nsfw.entity.YoupornVideo;
import com.github.lrhel.rabbot.utility.JsonReader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class YoupornApi implements HubApi {

    private static String baseUrl = "https://www.youporn.com/api/webmasters/";

    @Override
    public YoupornVideo getRandomVideo() {
        return getRandomVideo("");
    }

    @Override
    public YoupornVideo getRandomVideo(String search) {
        StringBuilder url = new StringBuilder(baseUrl);
        url.append("search/?");
        url.append("ordering=").append("mostviewed");
        url.append("&period=").append("monthly");
        url.append("search=").append(search);
        url.append("&thumbsize=").append("large");
        Random rng = new Random(System.currentTimeMillis());
        JSONObject result;
        int random;

        try {
            result = JsonReader.readJsonFromUrl(url.toString());
        } catch (Exception e) { e.printStackTrace(); return null; }

        try {
            JSONArray videos = result.getJSONArray("video");
            random = rng.nextInt(videos.length());
            for (int i = 0; i < videos.length(); i++) {
                if (i == random) {
                    return parseVideoFromJson(videos.getJSONObject(i));
                }
            }
        } catch (Exception e) { e.printStackTrace();return null; }
        return null;
    }

    private static YoupornVideo parseVideoFromJson(JSONObject video) {
        String duration = video.getString("duration");
        String video_id = video.getString("video_id");
        double rating = video.getDouble("rating");
        int ratings = video.getInt("ratings");
        String title = video.getString("title");
        String video_url = video.getString("url");
        String default_thumb = video.getString("default_thumb");

        return new YoupornVideo.YoupornVideoBuilder()
                .setTitle(title)
                .setVideo_id(video_id)
                .setDuration(duration)
                .setRating(rating)
                .setRatings(ratings)
                .setUrl(video_url)
                .setDefault_thumb(default_thumb).build();
    }

}
