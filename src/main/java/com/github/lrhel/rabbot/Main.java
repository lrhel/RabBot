package com.github.lrhel.rabbot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;

import com.github.lrhel.rabbot.config.Config;

import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;


public class Main {

	public static void main(String[] args) {
			
		//set logger and tracer
		FallbackLoggerConfiguration.setDebug(false);
		FallbackLoggerConfiguration.setTrace(false);
		
        DiscordApi api = new DiscordApiBuilder().setToken(Config.DISCORD.toString()).login().join();
        System.out.println("Logged in!");	
        System.out.println(api.createBotInvite());

        //Command Handler & Option
        CommandHandler cmd = new JavacordHandler(api);
        cmd.setDefaultPrefix("rb.");
        
        //Registering 
        cmd.registerCommand(new PingCommand());
        cmd.registerCommand(new SandyCommand());
        cmd.registerCommand(new HelpCommand(cmd));
        cmd.registerCommand(new ChallongeCommand());
        cmd.registerCommand(new EchoCommand());
        cmd.registerCommand(new StrawpollCommand());
        
        //Updating/Restarting stuff
        cmd.registerCommand(new UpdateCommand());
        cmd.registerCommand(new RestartCommand());
        
        // :EZ:
        cmd.registerCommand(new ShitpostingCommand());
        cmd.registerCommand(new CopypastaCommand());
        cmd.registerCommand(new ActiveCommand());



        
        //Join and Leave
        api.addServerJoinListener(event -> System.out.println("Joined server " + event.getServer().getName()));
        api.addServerLeaveListener(event -> System.out.println("Leaved server " + event.getServer().getName()));
	}

}
