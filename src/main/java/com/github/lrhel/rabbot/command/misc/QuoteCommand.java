package com.github.lrhel.rabbot.command.misc;

import de.kaleidox.javacord.util.commands.Command;

import com.github.lrhel.rabbot.config.Const;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.json.JSONObject;

public class QuoteCommand {
    @Command(description = "Get a random quote")
    public void quote(User user, TextChannel textChannel) {
        if (user.isBot()) {
            return;
        }

        textChannel.sendMessage(getQuote());
    }

    private static EmbedBuilder getQuote() {
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://quotes.p.mashape.com/")
                    .header("X-Mashape-Key", Const.MASHAPE_TOKEN)
                    .header("Accept", "application/json")
                    .asJson();
            JSONObject obj = response.getBody().getObject();
            return new EmbedBuilder()
                    .addInlineField(obj.getString("author"), obj.getString("quote"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new EmbedBuilder();
    }
}
