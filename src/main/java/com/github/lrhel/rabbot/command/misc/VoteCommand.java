package com.github.lrhel.rabbot.command.misc;

import java.util.ArrayList;
import java.util.Random;

import de.kaleidox.javacord.util.commands.Command;


public class VoteCommand {
    public static String voteLink = "https://discordbots.org/bot/441010449757110273/vote";

    @Command
    public String vote() {
        ArrayList<String> slogan = new ArrayList<>();
        slogan.add("Yeahhh Vote for RabBot ");
        slogan.add("RabBot 4 President ");
        slogan.add("A vote for who?\nA vote for RabBot");
        slogan.add("Thanks supporting me with ~~money~~ votes :rabbit: ");

        Random rng = new Random(System.currentTimeMillis());

        return slogan.get(rng.nextInt(slogan.size())) + voteLink;
    }
}
