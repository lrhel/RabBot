package com.github.lrhel.rabbot.command.moderation;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.awt.*;

public class HelpCommand implements CommandExecutor {

    private final CommandHandler commandHandler;

    public HelpCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Command(aliases = {"help", "commands"}, description = "Shows this page!")
    public void onHelpCommand(TextChannel textChannel, Server server, User user, DiscordApi api) {

        EmbedBuilder embedBuilder = new EmbedBuilder().setAuthor("Help Page", "", "");

        getInfo(embedBuilder);
        getGames(embedBuilder);
        getMoney(embedBuilder);
        getPokemon(embedBuilder);
        getMisc(embedBuilder);

        if(server.canYouKickUsers() || server.canYouBanUsers() || textChannel.canYouManageMessages()) {
            if (server.canBanUsers(user) || server.canKickUsers(user)
                    || server.getPermissions(user).getAllowedPermission().contains(PermissionType.MANAGE_MESSAGES)) {
                getModeration(embedBuilder, user, server);
            }
        }


        textChannel.sendMessage(embedBuilder.setColor(Color.CYAN));

    }

    private EmbedBuilder getInfo(EmbedBuilder embedBuilder) {
        StringBuilder sb = new StringBuilder();
        sb.append("**rb.info** *Show the info page*\n");
        sb.append("**rb.help** *Show this help page*\n");
        sb.append("**rb.invite** *Get an invite link for RabBot* :rabbit:");
        return embedBuilder.addField("__Info__", sb.toString());
    }

    private EmbedBuilder getGames(EmbedBuilder embedBuilder) {
        StringBuilder sb = new StringBuilder();
        sb.append("**rb.blackjack** *Play BlackJack against the Bot*\n");
        sb.append("**rb.roulette** *Play Europea Roulette*\n");
        sb.append("**rb.slotmachine** *Play Slotmachine 777*\n");
        sb.append("**rb.akinator** *[beta] Play with Akinator*\n");
        return embedBuilder.addField("__Games__", sb.toString());
    }

    private EmbedBuilder getMoney(EmbedBuilder embedBuilder) {
        StringBuilder sb = new StringBuilder();
        sb.append("**rb.daily** *Get your daily money*\n");
        sb.append("**rb.bonus** *Get a Bonus for Voting*\n");
        sb.append("**rb.givemoney** *Give money to someone*\n");
        return embedBuilder.addField("__Money__", sb.toString());
    }

    private EmbedBuilder getPokemon(EmbedBuilder embedBuilder) {
        StringBuilder sb = new StringBuilder();
        sb.append("**rb.pokemon** *Catch a random Pokemon*\n");
        sb.append("**rb.inventory** *View your Pokemon inventory*\n");
        return embedBuilder.addField("__Pokemon__", sb.toString());
    }

    private EmbedBuilder getMisc(EmbedBuilder embedBuilder) {
        StringBuilder sb = new StringBuilder();
        sb.append("**rb.rabbit** *Get a Rabbit Picture* :rabbit:\n");
        sb.append("**rb.image** *Get a Random Picture*\n");
        sb.append("**rb.shitposting** *Get a random shitpost*\n");
        sb.append("**rb.copypasta** *Get a random copypasta*\n");
        return embedBuilder.addField("__Misc__", sb.toString());
    }

    private EmbedBuilder getModeration(EmbedBuilder embedBuilder, User user, Server server) {
        StringBuilder sb = new StringBuilder();
        if (server.getPermissions(user).getAllowedPermission().contains(PermissionType.MANAGE_MESSAGES)) {
            sb.append("**rb.purge** *Purge message from the channel*\n");
        }
        if (server.canKickUsers(user) && server.canYouKickUsers()) {
            sb.append("**rb.kick** *Kick a member of the server*\n");
            sb.append("**rb.mute** *Mute a member of the server (give him all \"mute\" role)*\n");
        }
        if (server.canBanUsers(user) && server.canYouBanUsers()) {
            sb.append("**rb.ban** *Ban a member of the server*\n");
        }
        return embedBuilder.addField("__Moderation__", sb.toString());
    }
}