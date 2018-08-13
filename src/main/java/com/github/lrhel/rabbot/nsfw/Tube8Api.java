package com.github.lrhel.rabbot.nsfw;

import com.github.lrhel.rabbot.nsfw.entity.Tube8Video;
import com.github.lrhel.rabbot.utility.JsonReader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class Tube8Api implements HubApi {
    private enum Orientation {
        STRAIGHT ("straight"),
        GAY ("gay"),
        SHEMALE ("shemale"),
        DEFAULT ("straight,gay,shemale");

        String orientation = "";

        Orientation(String orientation) {
            this.orientation = orientation;
        }

        @Override
        public String toString() {
            return orientation;
        }
    }

    private static String baseUrl = "https://api.tube8.com/api.php?";

    public Tube8Api() {
    }

    @Override
    public Tube8Video getRandomVideo() {
        return getRandomVideo("");
    }

    @Override
    public Tube8Video getRandomVideo(String search) {
        return getRandomVideo(search, Orientation.DEFAULT);
    }

    public Tube8Video getRandomStraightVideo(String search) {
        return getRandomVideo(search, Orientation.STRAIGHT);
    }

    public Tube8Video getRandomGayVideo(String search) {
        return getRandomVideo(search, Orientation.GAY);
    }

    public Tube8Video getRandomShemaleVideo(String search) {
        return getRandomVideo(search, Orientation.SHEMALE);
    }

    @SuppressWarnings("Duplicates")
    public Tube8Video getRandomVideo(String search, Orientation orientation) {
        StringBuilder url = new StringBuilder(baseUrl);
        url.append("action=").append("searchVideos");
        url.append("&search=").append(search);
        url.append("&output=json");
        url.append("&thumbsize=").append("large");
        url.append("&ordering=").append("newest");
        url.append("&orientation=").append(orientation);
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
        } catch (Exception e) { return null; }
        return null;
    }

    private static Tube8Video parseVideoFromJson(JSONObject video) {
        JSONObject videoInfo = video.getJSONObject("video");
        String duration = videoInfo.getString("duration");
        String video_id = videoInfo.getString("video_id");
        double rating = videoInfo.getDouble("rating");
        int ratings = videoInfo.getInt("ratings");
        String title = video.getString("title");
        String video_url = videoInfo.getString("url");
        String default_thumb = videoInfo.getString("default_thumb");
        String orientation = videoInfo.getString("orientation");
        int views = videoInfo.getInt("views");

        return new Tube8Video.Tube8VideoBuilder().setTitle(title)
                .setVideo_id(video_id)
                .setDuration(duration)
                .setRating(rating)
                .setRatings(ratings)
                .setUrl(video_url)
                .setDefault_thumb(default_thumb)
                .setOrientation(orientation)
                .setViews(views)
                .build();
    }

}
