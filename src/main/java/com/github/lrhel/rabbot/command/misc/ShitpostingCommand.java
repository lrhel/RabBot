package com.github.lrhel.rabbot.command.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import de.kaleidox.javacord.util.commands.Command;

import com.github.lrhel.rabbot.config.Const;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;

public class ShitpostingCommand {

    @Command(aliases = {"shitposting", "shitpost", "sp"}, description = "Shitposting!", enablePrivateChat = false)
    public String onShitpostingCommand(User user, String[] arg, TextChannel ch, User usr) {
        if (user.isBot()) {
            return "";
        }

        int nbOfShitposting = 0;
        String path = "/home/koala/RabBot/shitposting.txt";
        if (arg.length > 0) {
            if (arg[0].equals("add") && (usr.isBotOwner()
                    || usr.getIdAsString().contentEquals(String.valueOf(Const.SMATH_ID))
                    || usr.getIdAsString().contentEquals(String.valueOf(Const.THUGA_ID)))) {
                try {
                    FileWriter fw = new FileWriter(new File(path), true);
                    for (int i = 1; i < arg.length; i++) {
                        fw.write(arg[i] + " ");
                    }
                    fw.write("\n");
                    fw.close();
                    ch.sendMessage("Shitposting added!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                Random rng = new Random(System.currentTimeMillis());
                List<String> line = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)))).lines().collect(Collectors.toList());
                return line.get(rng.nextInt(line.size()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }
}
