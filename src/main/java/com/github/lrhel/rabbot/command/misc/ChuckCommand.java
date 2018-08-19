package com.github.lrhel.rabbot.command.misc;

import com.github.lrhel.rabbot.utility.JsonReader;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.json.JSONObject;

public class ChuckCommand implements CommandExecutor {
    @Command(aliases = {"chuck"}, description = "Chuck Norris Joke")
    public void onChuckCommand(Message message, TextChannel textChannel) {
        if(message.getServerTextChannel().isPresent()) { //if send in a server
            if(!message.getServerTextChannel().get().isNsfw()) { //if it's not labelled nsfw
                textChannel.sendMessage(getJoke(false));
                return;
            }
        }
        textChannel.sendMessage(getJoke(true));
    }

    private static EmbedBuilder getJoke(boolean explicit) {
        StringBuilder url = new StringBuilder("http://api.icndb.com/jokes/random?escape=javascript&exclude=");
        if(explicit) {
            url.append("[explicit]");
        }
        try {
            JSONObject obj = JsonReader.readJsonFromUrl(url.toString());
            JSONObject joke = obj.getJSONObject("value");
            return new EmbedBuilder().addField("Chuck Norris", joke.getString("joke"));
        } catch (Exception e) { e.printStackTrace(); }
        return new EmbedBuilder();
    }
}
