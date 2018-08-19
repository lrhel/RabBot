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

public class TrumpCommand implements CommandExecutor {
    @Command(aliases = "trump", description = "Get a Random Trump quote")
    public void onTrumpCommand(User user, TextChannel textChannel) {
        if (user.isBot()) { return ; }

        textChannel.sendMessage(getTrumpQuote());
    }

    private static EmbedBuilder getTrumpQuote() {
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://matchilling-tronald-dump-v1.p.mashape.com/random/quote")
                    .header("X-Mashape-Key", Config.MASHAPE.toString())
                    .header("Accept", "application/hal+json")
                    .asJson();
            JSONObject obj = response.getBody().getObject();
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .addField(obj.getJSONObject("_embedded").getJSONArray("author").getJSONObject(0).getString("name"), obj.getString("value"))
                    .setFooter(obj.getJSONObject("_embedded").getJSONArray("source").getJSONObject(0).getString("url"))
                    ;
            return embedBuilder;
        } catch (Exception e) { e.printStackTrace(); }
        return new EmbedBuilder();
    }
}
