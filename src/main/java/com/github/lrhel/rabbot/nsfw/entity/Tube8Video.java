package com.github.lrhel.rabbot.nsfw.entity;

public class Tube8Video extends HubVideo {

    String orientation;

    private Tube8Video(String duration, String video_id, double rating, int ratings, String title, String url, String default_thumb, int views, String orientation) {
        super(duration, video_id, rating, ratings, title, url, default_thumb, views);
        this.orientation = orientation;
    }

    @Override
    public String getDuration () {
        try {
            int duration = Integer.parseInt(super.getDuration());
            return duration / 60 + ":" + ((duration % 60) < 10 ? "0" + duration % 60 : duration % 60);
        } catch (NumberFormatException ignored) {
            return super.getDuration();
        }
    }
    public static class Tube8VideoBuilder {

        private String duration;
        private String video_id;
        private double rating;
        private int ratings;
        private String title;
        private String url;
        private String default_thumb;
        private int views;
        private String orientation;

        public Tube8VideoBuilder setDuration(String duration) {
            this.duration = duration;
            return this;
        }

        public Tube8VideoBuilder setVideo_id(String video_id) {
            this.video_id = video_id;
            return this;
        }

        public Tube8VideoBuilder setRating(double rating) {
            this.rating = rating;
            return this;
        }

        public Tube8VideoBuilder setRatings(int ratings) {
            this.ratings = ratings;
            return this;
        }

        public Tube8VideoBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Tube8VideoBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Tube8VideoBuilder setDefault_thumb(String default_thumb) {
            this.default_thumb = default_thumb;
            return this;
        }

        public Tube8VideoBuilder setViews(int views) {
            this.views = views;
            return this;
        }

        public Tube8VideoBuilder setOrientation(String orientation) {
            this.orientation = orientation;
            return this;
        }

        public Tube8Video build() {
            return new Tube8Video(duration, video_id, rating, ratings, title, url, default_thumb, views, orientation);
        }
    }
}
