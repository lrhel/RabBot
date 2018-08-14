package com.github.lrhel.rabbot.command.misc;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;

import java.util.ArrayList;
import java.util.Random;


public class VoteCommand implements CommandExecutor {
    public static String voteLink = "https://discordbots.org/bot/441010449757110273/vote";

    @Command(aliases = {"vote"})
    public String onVoteCommand() {
        ArrayList<String> slogan = new ArrayList<>();
        slogan.add("Yeahhh Vote for RabBot ");
        slogan.add("RabBot 4 President ");
        slogan.add("A vote for who?\nA vote for RabBot");
        slogan.add("Thanks supporting me with ~~money~~ votes :rabbit: ");

        Random rng = new Random(System.currentTimeMillis());

        return slogan.get(rng.nextInt(slogan.size())) + voteLink;
    }
}
