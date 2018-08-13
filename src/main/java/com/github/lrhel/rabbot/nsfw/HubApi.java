package com.github.lrhel.rabbot.nsfw;

import com.github.lrhel.rabbot.nsfw.entity.HubVideo;

public interface HubApi {
    HubVideo getRandomVideo();

    HubVideo getRandomVideo(String search);
}
