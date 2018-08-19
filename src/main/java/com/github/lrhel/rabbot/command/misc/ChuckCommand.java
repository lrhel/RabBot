package com.github.lrhel.rabbot.command.misc;

import com.github.lrhel.rabbot.utility.JsonReader;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.json.JSONObject;

public class ChuckCommand implements CommandExecutor {
    @Command(aliases = {"chuck"}, description = "Chuck Norris Joke")
    public String onChuckCommand(ServerTextChannel serverTextChannel) {
        if(serverTextChannel.isNsfw()) {
            return getJoke(true);
        }
        else {
            return getJoke(false);
        }
    }

    private static String getJoke(boolean explicit) {
        StringBuilder url = new StringBuilder("http://api.icndb.com/jokes/random?escape=javascript&exclude=");
        if(explicit) {
            url.append("[explicit]");
        }
        try {
            JSONObject obj = JsonReader.readJsonFromUrl(url.toString());
            JSONObject joke = obj.getJSONObject("value");
            return joke.getString("joke");
        } catch (Exception e) { e.printStackTrace(); }
        return "";
    }
}
