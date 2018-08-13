package com.github.lrhel.rabbot.nsfw.entity;

public class YoupornVideo extends HubVideo {
    private YoupornVideo(String duration, String video_id, double rating, int ratings, String title, String url, String default_thumb, int views) {
        super(duration, video_id, rating, ratings, title, url, default_thumb, views);
    }

    public static class YoupornVideoBuilder {

        private String duration;
        private String video_id;
        private double rating;
        private int ratings;
        private String title;
        private String url;
        private String default_thumb;
        private int views;

        public YoupornVideoBuilder setDuration(String duration) {
            this.duration = duration;
            return this;
        }

        public YoupornVideoBuilder setVideo_id(String video_id) {
            this.video_id = video_id;
            return this;
        }

        public YoupornVideoBuilder setRating(double rating) {
            this.rating = rating;
            return this;
        }

        public YoupornVideoBuilder setRatings(int ratings) {
            this.ratings = ratings;
            return this;
        }

        public YoupornVideoBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public YoupornVideoBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public YoupornVideoBuilder setDefault_thumb(String default_thumb) {
            this.default_thumb = default_thumb;
            return this;
        }

        public YoupornVideoBuilder setViews(int views) {
            this.views = views;
            return this;
        }

        public YoupornVideo build() {
            return new YoupornVideo(duration, video_id, rating, ratings, title, url, default_thumb, views);
        }
    }
}
