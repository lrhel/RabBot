package com.github.lrhel.rabbot.command.nsfw;

import com.github.lrhel.rabbot.nsfw.Tube8Api;
import com.github.lrhel.rabbot.nsfw.entity.Tube8Video;
import com.github.lrhel.rabbot.utility.Utility;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.apache.commons.lang3.StringEscapeUtils;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.util.concurrent.TimeUnit;

public class Tube8Command implements CommandExecutor {
    @Command(aliases = {"tube8", "t8"})
    public void onTube8Command(User user, Message message, TextChannel textChannel, String[] args) {
        if (user.isBot()) { return ; }

        if(message.getServerTextChannel().isPresent()) { //if send in a server
            if(!message.getServerTextChannel().get().isNsfw()) { //if it's not labelled nsfw
                return;
            }
        }
        Tube8Api tube8Api = new Tube8Api();
        Tube8Video video;

        if (args.length > 0) {
            video = tube8Api.getRandomVideo(StringEscapeUtils.escapeHtml4(String.join("%20", args)));
        } else {
            video = tube8Api.getRandomVideo();
        }
        if (video == null) {
            textChannel.sendMessage("No video found. . .").thenAccept(Utility.getMessageDeleter(5, TimeUnit.SECONDS));
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setImage(video.getDefault_thumb())
                .addInlineField("From Tube8 4 You", video.toMarkdown())
                .setFooter("Duration: " + video.getDuration() + " - Ratings: " + video.getRating() + " - Voters: " + video.getRatings())
        ;
        textChannel.sendMessage(embedBuilder);
    }
}
