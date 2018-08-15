package com.github.lrhel.rabbot;

import com.github.lrhel.rabbot.command.admin.*;
import com.github.lrhel.rabbot.command.games.AkiCommand;
import com.github.lrhel.rabbot.command.games.BlackJackCommand;
import com.github.lrhel.rabbot.command.games.RouletteCommand;
import com.github.lrhel.rabbot.command.games.SlotMachineCommand;
import com.github.lrhel.rabbot.command.misc.*;
import com.github.lrhel.rabbot.command.moderation.*;
import com.github.lrhel.rabbot.command.money.BonusCommand;
import com.github.lrhel.rabbot.command.money.DailyCommand;
import com.github.lrhel.rabbot.command.money.GiveMoneyCommand;
import com.github.lrhel.rabbot.command.money.MoneyCommand;
import com.github.lrhel.rabbot.command.nsfw.PornhubCommand;
import com.github.lrhel.rabbot.command.nsfw.RedTubeCommand;
import com.github.lrhel.rabbot.command.nsfw.Tube8Command;
import com.github.lrhel.rabbot.command.nsfw.YouPornCommand;
import com.github.lrhel.rabbot.command.pokemon.DuplicateCommand;
import com.github.lrhel.rabbot.command.pokemon.InventoryCommand;
import com.github.lrhel.rabbot.command.pokemon.PokemonCommand;
import com.github.lrhel.rabbot.config.Config;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;
import org.discordbots.api.client.DiscordBotListAPI;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.PermissionsBuilder;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;


public class Main {

    public static void main(String[] args) {

        //set logger and tracer
        FallbackLoggerConfiguration.setDebug(false);
        FallbackLoggerConfiguration.setTrace(false);

        DiscordApi api = new DiscordApiBuilder().setToken(Config.DISCORD.toString()).login().join();
        System.out.println("Logged in!");
        System.out.println(api.createBotInvite(new PermissionsBuilder().setAllowed(
                PermissionType.READ_MESSAGES, PermissionType.ATTACH_FILE,
                PermissionType.SEND_MESSAGES, PermissionType.EMBED_LINKS,
                PermissionType.ADD_REACTIONS, PermissionType.READ_MESSAGE_HISTORY
        ).build()));

        DiscordBotListAPI discordBotListAPI = new DiscordBotListAPI.Builder()
                .token(Config.DISCORDLIST.toString())
                .botId(Config.BOTID.toString())
                .build();

        //Command Handler & Option
        CommandHandler cmd = new JavacordHandler(api);
        cmd.setDefaultPrefix("rb.");
        api.updateActivity("rb.help");

        //Information
        cmd.registerCommand(new HelpCommand(cmd));
        cmd.registerCommand(new InfoCommand());
        cmd.registerCommand(new InviteCommand());

        //Stupid Stuff
        cmd.registerCommand(new AkiCommand());
        cmd.registerCommand(new PingCommand());
        cmd.registerCommand(new SandyCommand());
        //cmd.registerCommand(new ChallongeCommand());
        cmd.registerCommand(new EchoCommand());
        cmd.registerCommand(new StrawpollCommand());
        cmd.registerCommand(new AsciiCommand());

        //Money Stuff
        cmd.registerCommand(new DailyCommand());
        cmd.registerCommand(new MoneyCommand());
        cmd.registerCommand(new RouletteCommand());
        cmd.registerCommand(new GiveMoneyCommand());
        cmd.registerCommand(new BlackJackCommand());
        cmd.registerCommand(new SlotMachineCommand());

        //Pokemon Stuff
        cmd.registerCommand(new PokemonCommand(discordBotListAPI));
        cmd.registerCommand(new InventoryCommand());
        cmd.registerCommand(new BonusCommand(discordBotListAPI));
        cmd.registerCommand(new DuplicateCommand());

        //Updating/Restarting stuff
        cmd.registerCommand(new UpdateCommand());
        cmd.registerCommand(new RestartCommand());
        cmd.registerCommand(new DisconnectCommand());

        // :EZ:
        cmd.registerCommand(new ShitpostingCommand());
        cmd.registerCommand(new CopypastaCommand());
        //cmd.registerCommand(new ActiveCommand());

        //Moderation Stuff
        cmd.registerCommand(new KickCommand());
        cmd.registerCommand(new BanCommand());
        cmd.registerCommand(new MuteCommand());
        cmd.registerCommand(new UnmuteCommand());
        cmd.registerCommand(new PurgeCommand());

        // Other stuff
        cmd.registerCommand(new RaidCommand());
        cmd.registerCommand(new GetServerCommand());
        cmd.registerCommand(new GetCommand(discordBotListAPI));
        cmd.registerCommand(new RabbitCommand());
        cmd.registerCommand(new UnsplashCommand());
        cmd.registerCommand(new SetServerCountCommand(discordBotListAPI, api));
        cmd.registerCommand(new ResetCommand());
        cmd.registerCommand(new VoteCommand());

        // NSFW
        cmd.registerCommand(new PornhubCommand());
        cmd.registerCommand(new Tube8Command());
        cmd.registerCommand(new RedTubeCommand());
        cmd.registerCommand(new YouPornCommand());

        //Join and Leave
        api.addServerJoinListener(event -> {
            System.out.println("Joined server " + event.getServer().getName());
            postServerCount(discordBotListAPI, api);
        });

        api.addServerLeaveListener(event -> {
            System.out.println("Leaved server " + event.getServer().getName());
            postServerCount(discordBotListAPI, api);
        });

    }

    public static void postServerCount(DiscordBotListAPI discordBotListAPI, DiscordApi discordApi) {
        discordBotListAPI.setStats(discordApi.getServers().size());
    }

}
