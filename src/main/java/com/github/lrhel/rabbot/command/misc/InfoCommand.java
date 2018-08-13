package com.github.lrhel.rabbot.command.misc;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.user.User;

import java.awt.*;

public class InfoCommand implements CommandExecutor {
    private static final String description = "(\\\\/) Rabbot the Bot that jump in your Server! (\\\\/)";

    @Command(aliases = {"info"}, description = "Show the info page!")
    public String onInfoCommand(User user, TextChannel textChannel, DiscordApi api) {
        if (user.isBot()) { return ""; }

        User bot = api.getYourself();
        String invite = api.createBotInvite(new PermissionsBuilder().setAllowed(
                PermissionType.READ_MESSAGES, PermissionType.ATTACH_FILE,
                PermissionType.SEND_MESSAGES, PermissionType.EMBED_LINKS,
                PermissionType.ADD_REACTIONS, PermissionType.READ_MESSAGE_HISTORY
        ).build());
        EmbedBuilder embed = new EmbedBuilder().setThumbnail(bot.getAvatar())
                .setAuthor(bot.getDiscriminatedName().replace(bot.getDiscriminator(), "").replace("#",""), api.createBotInvite(), "")
                .setDescription(description)
                .setColor(Color.GRAY)
                .addInlineField("Author", api.getOwner().join().getDiscriminatedName())
                .addInlineField("Guild count", String.valueOf(api.getServers().size()))
                .addInlineField("Support Server", "[Rabbot Support Server](https://discord.gg/N5c9zfP)")
                .addInlineField("Github's page", "[RabBot's Respository](https://github.com/lrhel/RabBot)")
                .addInlineField("Invite link", "[Bot's invite link](" + invite + ")")
                .addInlineField("Upvote", "[Vote4RabBot!](https://discordbots.org/bot/441010449757110273/vote)")
                .addField("API", apiList().toString())
                ;
        textChannel.sendMessage(embed);
        return "";

    }

    private StringBuilder apiList() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[Javacord](https://github.com/Javacord/Javacord)\n");
        stringBuilder.append("[PokeAPI](https://pokeapi.co/)\n");
        stringBuilder.append("[PokeKotlin](https://github.com/PokeAPI/pokekotlin)\n");
        stringBuilder.append("[Unsplash](https://www.unsplash.com)\n");
        stringBuilder.append("[AkiWrapper](https://github.com/markozajc/Akiwrapper)\n");
        stringBuilder.append("[DBL Java Library](https://github.com/DiscordBotList/DBL-Java-Library)\n");
        stringBuilder.append("[ASCII Art](https://asciiart.website)\n");
        stringBuilder.append("[StrawPoll Java API](https://github.com/Samuel-Maddock/StrawPoll-Java-API)\n");



        return stringBuilder;
    }
}
