package com.github.lrhel.rabbot.nsfw.entity;

public class HubVideo {


    private String duration;
    private String video_id;
    private double rating;
    private int ratings;
    private String title;
    private String url;
    private String default_thumb;
    private int views;


    public HubVideo(String duration, String video_id, double rating, int ratings, String title, String url, String default_thumb, int views) {
        this.duration = duration;
        this.video_id = video_id;
        this.rating = rating;
        this.ratings = ratings;
        this.title = title;
        this.url = url;
        this.default_thumb = default_thumb;
        this.views = views;
    }

    public String toMarkdown() {
        return "[" + title + "](" + url + ")";
    }

    public String getDuration() {
        return duration;
    }

    public String getVideo_id() {
        return video_id;
    }

    public double getRating() {
        return rating;
    }

    public int getRatings() {
        return ratings;
    }

    public String getTitle() {
        return title;
    }

    public int getViews() {
        return views;
    }

    public String getUrl() {
        return url;
    }

    public String getDefault_thumb() {
        return default_thumb;
    }

    public static class HubVideoBuilder {

        private String duration;
        private String video_id;
        private double rating;
        private int ratings;
        private String title;
        private String url;
        private String default_thumb;
        private int views;

        public HubVideoBuilder setDuration(String duration) {
            this.duration = duration;
            return this;
        }

        public HubVideoBuilder setVideo_id(String video_id) {
            this.video_id = video_id;
            return this;
        }

        public HubVideoBuilder setRating(double rating) {
            this.rating = rating;
            return this;
        }

        public HubVideoBuilder setRatings(int ratings) {
            this.ratings = ratings;
            return this;
        }

        public HubVideoBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public HubVideoBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public HubVideoBuilder setDefault_thumb(String default_thumb) {
            this.default_thumb = default_thumb;
            return this;
        }

        public HubVideoBuilder setViews(int views) {
            this.views = views;
            return this;
        }

        public HubVideo build() {
            return new HubVideo(duration, video_id, rating, ratings, title, url, default_thumb, views);
        }
    }


}
