package com.github.lrhel.rabbot.command.moderation;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class HelpCommand implements CommandExecutor {

    private final CommandHandler commandHandler;

    public HelpCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Command(aliases = {"help", "commands"}, description = "Shows this page!")
    public void onHelpCommand(TextChannel textChannel, Server server, User user, DiscordApi api) {

        if (user.isBot()) { return; }

        EmbedBuilder embedBuilder = new EmbedBuilder().setAuthor("Help Page", "", "");

        getInfo(embedBuilder);
        getGames(embedBuilder);
        getMoney(embedBuilder);
        getPokemon(embedBuilder);
        getMisc(embedBuilder);
        getNsfw(embedBuilder, server);

        if(server.canYouKickUsers() || server.canYouBanUsers() || textChannel.canYouManageMessages()) {
            if (server.canBanUsers(user) || server.canKickUsers(user)
                    || server.getPermissions(user).getAllowedPermission().contains(PermissionType.MANAGE_MESSAGES)) {
                getModeration(embedBuilder, user, server);
            }
        }

        embedBuilder.setFooter("Also don't forget our rb.bonus (\\/)");

        textChannel.sendMessage(embedBuilder.setColor(Color.CYAN));

    }

    private EmbedBuilder getInfo(@NotNull EmbedBuilder embedBuilder) {
        StringBuilder sb = new StringBuilder();
        sb.append("**rb.info** *Show the info page*\n");
        sb.append("**rb.help** *Show this help page*\n");
        sb.append("**rb.invite** *Get an invite link for RabBot* :rabbit:\n");
        return embedBuilder.addField("__Info__", sb.toString());
    }

    private EmbedBuilder getGames(@NotNull EmbedBuilder embedBuilder) {
        StringBuilder sb = new StringBuilder();
        sb.append("**rb.blackjack** *Play BlackJack against the Bot*\n");
        sb.append("**rb.roulette** *Play Europea Roulette*\n");
        sb.append("**rb.slotmachine** *Play Slotmachine 777*\n");
        //sb.append("**rb.akinator** *[Beta] Play with Akinator ~~really broky~~*\n");
        return embedBuilder.addField("__Games__", sb.toString());
    }

    private EmbedBuilder getMoney(@NotNull EmbedBuilder embedBuilder) {
        StringBuilder sb = new StringBuilder();
        sb.append("**rb.daily** *Get your daily money*\n");
        sb.append("**rb.money** *Get your current balance*\n");
        sb.append("**rb.bonus** *Get a Bonus for Voting*\n");
        sb.append("**rb.givemoney** *Give money to someone*\n");
        return embedBuilder.addField("__Money__", sb.toString());
    }

    private EmbedBuilder getPokemon(@NotNull EmbedBuilder embedBuilder) {
        StringBuilder sb = new StringBuilder();
        sb.append("**rb.pokemon** *Catch a random Pokemon*\n");
        sb.append("**rb.inventory** *View your Pokemon inventory*\n");
        return embedBuilder.addField("__Pokemon__", sb.toString());
    }

    private EmbedBuilder getMisc(@NotNull EmbedBuilder embedBuilder) {
        StringBuilder sb = new StringBuilder();
        sb.append("**rb.strawpoll** *Make or Check a StrawPoll*\n");
        sb.append("**rb.rabbit** *Get a Rabbit Picture* :rabbit:\n");
        sb.append("**rb.image** *Get a Random Picture*\n");
        sb.append("**rb.shitposting** *Get a random shitpost*\n");
        sb.append("**rb.copypasta** *Get a random copypasta*\n");
        sb.append("**rb.ascii** *Get a random ASCII picture*\n");
        sb.append("**rb.chuck** *Get a ~~joke~~ fact about Chuck Norris*\n");
        sb.append("**rb.vote** *Retrieve the RabBot vote link*\n");
        return embedBuilder.addField("__Misc__", sb.toString());
    }

    private EmbedBuilder getModeration(EmbedBuilder embedBuilder, User user, @NotNull Server server) {
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

    private EmbedBuilder getNsfw(EmbedBuilder embedBuilder, @NotNull Server server) {
        if(server.getTextChannels().stream().anyMatch(ServerTextChannel::isNsfw)) {
            StringBuilder sb = new StringBuilder();
            sb.append("*Only in nsfw channel ;)*\n");
            sb.append("**rb.pornhub** *Get a random video from PornHub*\n");
            sb.append("**rb.youporn** *Get a random video from YouPorn*\n");
            sb.append("**rb.redtube** *Get a random video from RedTube*\n");
            sb.append("**rb.tube8** *[Beta] Get a random video from Tube8*\n");

            return embedBuilder.addField("__Nsfw__", sb.toString());
        }
        return embedBuilder;
    }
}
