package com.github.lrhel.rabbot.command.misc;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.awt.*;

public class InfoCommand implements CommandExecutor {
    private static final String description = "(\\\\/) Rabbot the Bot that jump in your Server! (\\\\/)";

    @Command(aliases = {"info"}, description = "Show the info page!")
    public String onInfoCommand(TextChannel textChannel, DiscordApi api) {
        User bot = api.getYourself();
        EmbedBuilder embed = new EmbedBuilder().setThumbnail(bot.getAvatar())
                .setAuthor(bot.getDiscriminatedName().replace(bot.getDiscriminator(), "").replace("#",""), api.createBotInvite(), "")
                .setDescription(description)
                .setColor(Color.GRAY)
                .addInlineField("Author", api.getOwner().join().getDiscriminatedName())
                .addInlineField("Guild count", String.valueOf(api.getServers().size()))
                .addInlineField("Support Server", "[Rabbot Support Server](https://discord.gg/g88Wrfa)")
                .addInlineField("Github's page", "[Rabbot's Respository](https://github.com/lrhel/RabBot)")
                .addInlineField("Invite link", "[Bot's invite link](" + api.createBotInvite() + ")")
                .addField("API", apiList().toString())
                ;
        textChannel.sendMessage(embed);
        return "";

    }

    private StringBuilder apiList() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[Javacord](https://github.com/Javacord/Javacord)\n");
        stringBuilder.append("[PokeAPI](https://pokeapi.co/)\n");

        return stringBuilder;
    }
}
