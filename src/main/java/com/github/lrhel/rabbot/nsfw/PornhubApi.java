package com.github.lrhel.rabbot.nsfw;

import com.github.lrhel.rabbot.nsfw.entity.PornhubVideo;
import com.github.lrhel.rabbot.utility.JsonReader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class PornhubApi implements HubApi{

    private static String baseUrl = "https://www.pornhub.com/webmasters/";

    private String apiKey;

    private PornhubApi(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Get a random Video from Pornhub
     * @return a PornhubVideo containing the information about the video or null if no video found
     */
    public PornhubVideo getRandomVideo() {
        return getRandomVideo("");
    }

    /**
     * Get a random Video from Pornhub with a specific search
     * @param search the search term for the Video
     * @return a PornhubVideo containing the information about the video or null if no video found
     */
    @SuppressWarnings("Duplicates")
    public PornhubVideo getRandomVideo(String search) {
        StringBuilder url = new StringBuilder(baseUrl);
        url.append("search?").append(this.apiKey);
        url.append("&search=").append(search);
        url.append("&thumbsize=").append("large");
        url.append("&ordering=").append("newest");
        Random rng = new Random(System.currentTimeMillis());
        JSONObject result;
        int random;

        try {
            result = JsonReader.readJsonFromUrl(url.toString());
        } catch (Exception e) { e.printStackTrace(); return null; }

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

    private static PornhubVideo parseVideoFromJson(JSONObject video) {
        String duration = video.getString("duration");
        String video_id = video.getString("video_id");
        double rating = video.getDouble("rating");
        int ratings = video.getInt("ratings");
        String title = video.getString("title");
        String video_url = video.getString("url");
        String default_thumb = video.getString("default_thumb");

        return new PornhubVideo.PornhubVideoBuilder()
                .setTitle(title)
                .setVideo_id(video_id)
                .setDuration(duration)
                .setRating(rating)
                .setRatings(ratings)
                .setUrl(video_url)
                .setDefault_thumb(default_thumb).createPornhubVideo();
    }

    public static class PornhubApiBuilder {

        private String apiKey;

        public PornhubApiBuilder setApiKey(String apiKey) {
            this.apiKey = "id=" + apiKey;
            return this;
        }

        public PornhubApi build() {
            return new PornhubApi(apiKey);
        }
    }

}
