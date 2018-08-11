package com.github.lrhel.rabbot.command.admin;

import com.github.lrhel.rabbot.Money;
import com.github.lrhel.rabbot.config.Config;
import com.github.lrhel.rabbot.dblApi.DblApi;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.discordbots.api.client.DiscordBotListAPI;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;

import static com.github.lrhel.rabbot.command.pokemon.RabbotPokemon.totalCatchedPokemon;
import static com.github.lrhel.rabbot.command.pokemon.RabbotPokemon.totalUniqueCatchedPokemon;

public class GetCommand implements CommandExecutor {
    DiscordBotListAPI discordBotListAPI;

    public GetCommand(DiscordBotListAPI api) {
        this.discordBotListAPI = api;
    }

    @Command(aliases = {"get"}, showInHelpPage = false)
    public String onGetCommand(User user, String[] arg, DiscordApi api, Message message, TextChannel textChannel){
        StringBuilder sb;
        DblApi dblApi = new DblApi().setId(Config.BOTID.toString());


        if (!user.isBotOwner() || arg.length == 0)
            return "";
        switch (arg[0]) {
            case "server":
                ArrayList<Server> servers = new ArrayList<>(api.getServers());
                StringBuilder list = new StringBuilder("```\n");
                for (Server server : servers) {
                    list.append(server.getName().length() > 16 ? server.getName().subSequence(0, 16) : server.getName());
                    for (int i = 16 - server.getName().length(); i > 0; i--)
                        list.append(" ");
                    list.append(" | ");
                    try {
                        list.append(server.getInvites().join().iterator().next().getUrl().toExternalForm());
                    } catch (Exception e) {
                        list.append("No invitation");
                    }
                    list.append("\n");
                }
                list.append("```");
                return list.toString();
            case "inv":
            case "invite":
                return api.createBotInvite(new PermissionsBuilder().setAllowed(
                        PermissionType.READ_MESSAGES, PermissionType.ATTACH_FILE,
                        PermissionType.SEND_MESSAGES, PermissionType.EMBED_LINKS,
                        PermissionType.ADD_REACTIONS, PermissionType.READ_MESSAGE_HISTORY
                ).build());
            case "money":
                sb = new StringBuilder();
                for (User usr : message.getMentionedUsers()) {
                    sb.append(usr.getName()).append(": ").append(String.valueOf(Money.getMoney(usr) + "$\n"));
                }
                return sb.toString();
            case "pokemon":
                try {
                    sb = new StringBuilder();
                    for (User usr : message.getMentionedUsers()) {
                        sb.append(usr.getName()).append(": ").append(totalCatchedPokemon(usr))
                                .append("(Unique Pokemon: ").append(totalUniqueCatchedPokemon(usr))
                                .append(")").append("\n");

                    }
                    sb.append("Total catch Pokemon: ").append(totalCatchedPokemon()).append("\n");
                    sb.append("Total unique catch Pokemon: ").append(totalUniqueCatchedPokemon());
                    return sb.toString();
                } catch (Exception ignored) { }
            case "hasvoted":
                final StringBuilder stringBuilder = new StringBuilder();
                try {
                    for (User usr : message.getMentionedUsers()) {
                        stringBuilder.append(usr.getName()).append(": ");
                        discordBotListAPI.hasVoted(usr.getIdAsString()).whenComplete((voted, throwable) -> {
                            if(voted) {
                                textChannel.sendMessage(stringBuilder.append("voted").toString());
                            } else {
                                textChannel.sendMessage(stringBuilder.append("not voted").toString());
                            }
                        });
                    }
                    return "";
                } catch (Exception ignored) { }
        }
        return "";
    }
}
