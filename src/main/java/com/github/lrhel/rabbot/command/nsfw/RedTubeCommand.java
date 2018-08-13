package com.github.lrhel.rabbot.command.nsfw;

import com.github.lrhel.rabbot.nsfw.RedTubeApi;
import com.github.lrhel.rabbot.nsfw.entity.RedTubeVideo;
import com.github.lrhel.rabbot.utility.Utility;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.apache.commons.lang3.StringEscapeUtils;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.util.concurrent.TimeUnit;

public class RedTubeCommand implements CommandExecutor {
    @Command(aliases = {"redtube", "rt"})
    public void onRedTubeCommand(User user, Message message, TextChannel textChannel, String[] args) {
        if (user.isBot()) { return ; }

        if(message.getServerTextChannel().isPresent()) { //if send in a server
            if(!message.getServerTextChannel().get().isNsfw()) { //if it's not labelled nsfw
                return;
            }
        }
        RedTubeApi redTubeApi = new RedTubeApi();
        RedTubeVideo video;

        if (args.length > 0) {
            video = redTubeApi.getRandomVideo(StringEscapeUtils.escapeHtml4(String.join("%20", args)));
        } else {
            video = redTubeApi.getRandomVideo();
        }
        if (video == null) {
            textChannel.sendMessage("No video found. . .").thenAccept(Utility.getMessageDeleter(5, TimeUnit.SECONDS));
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setImage(video.getDefault_thumb())
                .addInlineField("From RedTube 4 You", video.toMarkdown())
                .setFooter("Duration: " + video.getDuration() + " - Ratings: " + video.getRating() + " - Voters: " + video.getRatings())
        ;
        textChannel.sendMessage(embedBuilder);
    }
}
