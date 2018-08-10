package com.github.lrhel.rabbot.command.money;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.discordbots.api.client.DiscordBotListAPI;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

public class BonusCommand implements CommandExecutor {
    private DiscordBotListAPI discordBotListAPI;

    public BonusCommand(DiscordBotListAPI dblapi) {
        this.discordBotListAPI = dblapi;
    }

    @Command(aliases = {"bonus"}, description = "Bonus if you voted the Bot")
    public void onBonusCommand(User user, TextChannel textChannel, DiscordApi api) {
        discordBotListAPI.hasVoted(user.getIdAsString()).whenComplete((aBoolean, throwable) -> {
           if (aBoolean) {
               EmbedBuilder embedBuilder = new EmbedBuilder().setAuthor("RabBot's Bonus", "https://discordbots.org/bot/441010449757110273", api.getYourself().getAvatar())
                                                    .addField("","");
           } else {

           }
        });
    }
}
