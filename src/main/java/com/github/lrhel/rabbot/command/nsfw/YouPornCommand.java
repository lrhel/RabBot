package com.github.lrhel.rabbot.command.nsfw;

import java.util.concurrent.TimeUnit;

import de.kaleidox.javacord.util.commands.Command;

import com.github.lrhel.rabbot.nsfw.YoupornApi;
import com.github.lrhel.rabbot.nsfw.entity.YoupornVideo;
import com.github.lrhel.rabbot.utility.Utility;
import org.apache.commons.lang3.StringEscapeUtils;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

public class YouPornCommand {
    @Command(aliases = {"youporn", "yp"})
    public void onYouPornCommand(User user, Message message, TextChannel textChannel, String[] args) {
        if (user.isBot()) {
            return;
        }

        if (message.getServerTextChannel().isPresent()) { //if send in a server
            if (!message.getServerTextChannel().get().isNsfw()) { //if it's not labelled nsfw
                return;
            }
        }
        YoupornApi youpornApi = new YoupornApi();
        YoupornVideo video;

        if (args.length > 0) {
            video = youpornApi.getRandomVideo(StringEscapeUtils.escapeHtml4(String.join("%20", args)));
        } else {
            video = youpornApi.getRandomVideo();
        }
        if (video == null) {
            textChannel.sendMessage("No video found. . .").thenAccept(Utility.getMessageDeleter(5, TimeUnit.SECONDS));
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setImage(video.getDefault_thumb())
                .addInlineField("From PornHub 4 You", video.toMarkdown())
                .setFooter("Duration: " + video.getDuration() + " - Ratings: " + video.getRating() + " - Voters: " + video.getRatings())
        ;
        textChannel.sendMessage(embedBuilder);
    }
}
