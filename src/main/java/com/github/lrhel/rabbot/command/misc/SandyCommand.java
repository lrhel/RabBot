package com.github.lrhel.rabbot.command.misc;

import java.util.Random;

import de.kaleidox.javacord.util.commands.Command;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

public class SandyCommand {

    @Command(description = "Complimente Sandy!", enablePrivateChat = false, shownInHelpCommand = false)
    public String sandy(User user, TextChannel ch) {
        if (user.isBot()) {
            return "";
        }

        MessageBuilder mbuilder = new MessageBuilder();
        EmbedBuilder ebuilder = new EmbedBuilder();
        ebuilder.setTitle("WOW!!")
                .setDescription(SandyCommand.getCompliment());
        mbuilder.setEmbed(ebuilder);
        ch.sendMessage(ebuilder);
        return "";
    }

    private static String getCompliment() {
        String[] table = {"belle", "gentille", "sympatoch", "mignonne", "magnifique", "divine", "authentique", "geniiiaaaale", "kawaiiii"};
        Random rng = new Random(System.currentTimeMillis());
        return "Sandy est la plus " + table[rng.nextInt(table.length)];
    }

}

	