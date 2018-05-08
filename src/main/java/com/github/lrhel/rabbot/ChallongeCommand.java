package com.github.lrhel.rabbot;

import org.javacord.api.entity.user.User;

//import com.exsoloscript.challonge.Challonge;
//import com.exsoloscript.challonge.ChallongeApi;
//import com.github.lrhel.rabbot.config.Config;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

public class ChallongeCommand implements CommandExecutor {
	//ChallongeApi api = Challonge.getFor("LRHel", Config.CHALLONGE.toString());
	
	@Command(aliases = {"challonge"}, privateMessages = false, description = "Create a challonge tournament!", showInHelpPage = false)
	public String onChallongeCommand(String[] arg, User user) {
		
		return "";
	}
}
