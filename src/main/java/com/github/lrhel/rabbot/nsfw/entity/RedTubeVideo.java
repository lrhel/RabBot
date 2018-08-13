package com.github.lrhel.rabbot.nsfw.entity;

public class RedTubeVideo extends HubVideo {
    private RedTubeVideo(String duration, String video_id, double rating, int ratings, String title, String url, String default_thumb, int views) {
        super(duration, video_id, rating, ratings, title, url, default_thumb, views);
    }

    public static class RedTubeVideoBuilder {

        private String duration;
        private String video_id;
        private double rating;
        private int ratings;
        private String title;
        private String url;
        private String default_thumb;
        private int views;

        public RedTubeVideoBuilder setDuration(String duration) {
            this.duration = duration;
            return this;
        }

        public RedTubeVideoBuilder setVideo_id(String video_id) {
            this.video_id = video_id;
            return this;
        }

        public RedTubeVideoBuilder setRating(double rating) {
            this.rating = rating;
            return this;
        }

        public RedTubeVideoBuilder setRatings(int ratings) {
            this.ratings = ratings;
            return this;
        }

        public RedTubeVideoBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public RedTubeVideoBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public RedTubeVideoBuilder setDefault_thumb(String default_thumb) {
            this.default_thumb = default_thumb;
            return this;
        }

        public RedTubeVideoBuilder setViews(int views) {
            this.views = views;
            return this;
        }

        public RedTubeVideo build() {
            return new RedTubeVideo(duration, video_id, rating, ratings, title, url, default_thumb, views);
        }
    }
}
