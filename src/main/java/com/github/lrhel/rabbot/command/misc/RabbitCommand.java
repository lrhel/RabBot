package com.github.lrhel.rabbot.command.misc;

import com.github.lrhel.rabbot.config.Config;
import com.github.lrhel.rabbot.unsplash.UnsplashApi;
import com.github.lrhel.rabbot.unsplash.entity.UnsplashImage;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

public class RabbitCommand implements CommandExecutor {
    @Command(aliases = {"rabbit", "rb"}, description = "Show a Rabbit picture")
    public void onRabbitCommand(User user, TextChannel textChannel) {
        if (user.isBot()) { return ; }

        UnsplashApi unsplashApi = new UnsplashApi(Config.UNSPLASH.toString());
        try {
            UnsplashImage image = unsplashApi.getRandomImage("bunny");
            String url = image.getUrls().get("full");
            EmbedBuilder embed = new EmbedBuilder().setAuthor("(\\/) Author: " + image.getAuthor().getName(), "https://unsplash.com/@" + image.getAuthor().getUsername() + "?utm_source=RabBot&utm_medium=referral", "")
                    .setImage(url)
                    .setColor(image.getColor())
                    .setFooter("Powered by Unsplash.com", "https://cdn-images-1.medium.com/letterbox/42/36/50/50/1*TWuSZbKAMGfRYVaYy2PvMA.png?source=logoAvatar-lo_KIiiIO0WHepJ---1597a02d5bf9");

            textChannel.sendMessage(embed);
        } catch (Exception ignored) { }
    }
}
