package com.github.lrhel.rabbot.command.misc;

import de.kaleidox.javacord.util.commands.Command;

import com.github.lrhel.rabbot.config.Const;
import com.github.lrhel.rabbot.unsplash.UnsplashApi;
import com.github.lrhel.rabbot.unsplash.entity.UnsplashImage;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

public class UnsplashCommand {
    @Command(aliases = {"image", "img", "unsplash"}, description = "Splash a random image from Unsplash")
    public void onUnsplashCommand(User user, TextChannel textChannel) {
        if (user.isBot()) {
            return;
        }

        UnsplashApi unsplashApi = new UnsplashApi(Const.UNSPLASH_TOKEN);
        try {
            UnsplashImage image = unsplashApi.getRandomImage();
            String url = image.getUrls().get("full");
            EmbedBuilder embed = new EmbedBuilder().setAuthor("Author: " + image.getAuthor().getName(), "https://unsplash.com/@" + image.getAuthor().getUsername() + "?utm_source=RabBot&utm_medium=referral", "")
                    .setImage(url)
                    .setColor(image.getColor())
                    .setFooter("Powered by Unsplash.com", "https://cdn-images-1.medium.com/letterbox/42/36/50/50/1*TWuSZbKAMGfRYVaYy2PvMA.png?source=logoAvatar-lo_KIiiIO0WHepJ---1597a02d5bf9");

            textChannel.sendMessage(embed);
        } catch (Exception ignored) {
        }
    }
}
