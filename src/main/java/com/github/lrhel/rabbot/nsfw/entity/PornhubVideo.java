package com.github.lrhel.rabbot.nsfw.entity;

public class PornhubVideo extends HubVideo{


    private PornhubVideo(String duration, String video_id, double rating, int ratings, String title, String url, String default_thumb, int views) {
        super(duration, video_id, rating, ratings, title, url, default_thumb, views);
    }

    public static class PornhubVideoBuilder {

        private String duration;
        private String video_id;
        private double rating;
        private int ratings;
        private String title;
        private String url;
        private String default_thumb;
        private int views;

        public PornhubVideoBuilder setDuration(String duration) {
            this.duration = duration;
            return this;
        }

        public PornhubVideoBuilder setVideo_id(String video_id) {
            this.video_id = video_id;
            return this;
        }

        public PornhubVideoBuilder setRating(double rating) {
            this.rating = rating;
            return this;
        }

        public PornhubVideoBuilder setRatings(int ratings) {
            this.ratings = ratings;
            return this;
        }

        public PornhubVideoBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public PornhubVideoBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public PornhubVideoBuilder setDefault_thumb(String default_thumb) {
            this.default_thumb = default_thumb;
            return this;
        }

        public PornhubVideoBuilder setViews(int views) {
            this.views = views;
            return this;
        }

        public PornhubVideo createPornhubVideo() {
            return new PornhubVideo(duration, video_id, rating, ratings, title, url, default_thumb, views);
        }
    }
}
