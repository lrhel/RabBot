package com.github.lrhel.rabbot.command.misc;

import de.kaleidox.javacord.util.commands.Command;

import org.javacord.api.entity.user.User;

//import com.exsoloscript.challonge.Challonge;
//import com.exsoloscript.challonge.ChallongeApi;
//import com.github.lrhel.rabbot.config.Config;

public class ChallongeCommand {
    //ChallongeApi api = Challonge.getFor("LRHel", Config.CHALLONGE.toString());

    @Command(enablePrivateChat = false, description = "Create a challonge tournament!", shownInHelpCommand = false)
    public String challonge(String[] arg, User user) {

        return "";
    }
}
