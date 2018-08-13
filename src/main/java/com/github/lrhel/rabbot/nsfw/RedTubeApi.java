package com.github.lrhel.rabbot.nsfw;

import com.github.lrhel.rabbot.nsfw.entity.RedTubeVideo;
import com.github.lrhel.rabbot.utility.JsonReader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class RedTubeApi implements HubApi {
    private static String baseUrl = "https://api.redtube.com/?output=json";

    @Override
    public RedTubeVideo getRandomVideo() {
        return getRandomVideo("");
    }

    @Override
    public RedTubeVideo getRandomVideo(String search) {
        StringBuilder url = new StringBuilder(baseUrl);
        url.append("&data=").append("redtube.Videos.searchVideos");
        url.append("&search=").append(search);
        url.append("&thumbsize=").append("big");
        Random rng = new Random(System.currentTimeMillis());
        JSONObject result;
        int random;

        try {
            result = JsonReader.readJsonFromUrl(url.toString());
        } catch (Exception e) { return null; }

        try {
            JSONArray videos = result.getJSONArray("videos");
            random = rng.nextInt(videos.length());
            for (int i = 0; i < videos.length(); i++) {
                if (i == random) {
                    return parseVideoFromJson(videos.getJSONObject(i));
                }
            }
        } catch (Exception e) { e.printStackTrace();return null; }
        return null;
    }

    public static RedTubeVideo parseVideoFromJson(JSONObject video) {
        JSONObject videoInfo = video.getJSONObject("video");
        String duration = videoInfo.getString("duration");
        String video_id = videoInfo.getString("video_id");
        double rating = videoInfo.getDouble("rating");
        int ratings = videoInfo.getInt("ratings");
        String title = videoInfo.getString("title");
        String video_url = videoInfo.getString("url");
        String default_thumb = videoInfo.getString("default_thumb");
        int views = videoInfo.getInt("views");

        return new RedTubeVideo.RedTubeVideoBuilder().setTitle(title)
                .setVideo_id(video_id)
                .setDuration(duration)
                .setRating(rating)
                .setRatings(ratings)
                .setUrl(video_url)
                .setDefault_thumb(default_thumb)
                .setViews(views)
                .build();
    }

}
