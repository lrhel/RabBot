package com.github.lrhel.rabbot.command.misc;

import com.github.lrhel.rabbot.config.Config;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.json.JSONObject;

public class QuoteCommand implements CommandExecutor {
    @Command(aliases = {"quote"}, description = "Get a random quote")
    public void onQuoteCommand(User user, TextChannel textChannel) {
        if (user.isBot()) { return ; }

        textChannel.sendMessage(getQuote());
    }

    private static EmbedBuilder getQuote() {
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://quotes.p.mashape.com/")
                    .header("X-Mashape-Key", Config.MASHAPE.toString())
                    .header("Accept", "application/json")
                    .asJson();
            JSONObject obj = response.getBody().getObject();
            return new EmbedBuilder()
                    .addInlineField(obj.getString("author"), obj.getString("quote"));

        } catch (Exception e) { e.printStackTrace(); }
        return new EmbedBuilder();
    }
}
