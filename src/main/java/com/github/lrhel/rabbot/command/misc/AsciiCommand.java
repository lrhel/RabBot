package com.github.lrhel.rabbot.command.misc;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;

import java.util.Random;

public class AsciiCommand implements CommandExecutor {
    @Command(aliases = {"ascii"})
    public void onAsciiCommand(User user, TextChannel textChannel) {
        if (user.isBot()) { return ; }
        textChannel.sendMessage(randomRabbit());
    }

    private String randomRabbit() {
        Random rng = new Random(System.currentTimeMillis());
        switch (rng.nextInt(6)) {
            case 0: return "```\n" +
                    "             ,\n" +
                    "            /|      __\n" +
                    "           / |   ,-~ /\n" +
                    "          Y :|  //  /\n" +
                    "          | jj /( .^\n" +
                    "          >-\"~\"-v\"\n" +
                    "         /       Y\n" +
                    "        jo  o    |\n" +
                    "       ( ~T~     j\n" +
                    "        >._-' _./\n" +
                    "       /   \"~\"  |\n" +
                    "      Y     _,  |\n" +
                    "     /| ;-\"~ _  l\n" +
                    "    / l/ ,-\"~    \\\n" +
                    "    \\//\\/      .- \\\n" +
                    "     Y        /    Y    -Row\n" +
                    "     l       I     !\n" +
                    "     ]\\      _\\    /\"\\\n" +
                    "    (\" ~----( ~   Y.  )\n" +
                    "~~~~~~~~~~~~~~~~~~~~~~~~~~```";
            case 1: return "```" +
                    "                          .\".\n" +
                    "                         /  |\n" +
                    "                        /  /\n" +
                    "                       / ,\"\n" +
                    "           .-------.--- /\n" +
                    "          \"._ __.-/ o. o\\  \n" +
                    "             \"   (    Y  )\n" +
                    "                  )     /\n" +
                    "                 /     (\n" +
                    "                /       Y\n" +
                    "            .-\"         |\n" +
                    "           /  _     \\    \\ \n" +
                    "          /    `. \". ) /' )\n" +
                    "         Y       )( / /(,/\n" +
                    "        ,|      /     )\n" +
                    "       ( |     /     /\n" +
                    "        \" \\_  (__   (__        \n" +
                    "            \"-._,)--._,)\n" +
                    "```";
            case 2: return "```" +
                    "               (`.         ,-,\n" +
                    "               `\\ `.    ,;' /\n" +
                    "                \\`. \\ ,'/ .'\n" +
                    "          __     `.\\ Y /.'\n" +
                    "       .-'  ''--.._` ` (\n" +
                    "     .'            /   `\n" +
                    "    ,           ` '   Q '\n" +
                    "    ,         ,   `._    \\\n" +
                    "    |         '     `-.;_'\n" +
                    "    `  ;    `  ` --,.._;\n" +
                    "    `    ,   )   .'\n" +
                    "     `._ ,  '   /_\n" +
                    "        ; ,''-,;' ``-\n" +
                    "         ``-..__\\``--`  ```";
            case 3: return "```" +
                    "            .--,_\n" +
                    "           / ,/ /\n" +
                    "          / // /\n" +
                    "         / // /\n" +
                    "       .'  ' (\n" +
                    "      /       \\.-\"\"\"-._\n" +
                    "     / a   ' .    '    `-.\n" +
                    "    (       .  '      \"   `.\n" +
                    "     `-.-'       \"       '  ;\n" +
                    "         `.'  \"  .  .-'    \" ;\n" +
                    "          : .     .'          ;\n" +
                    "          `.   ' :     '   '  ;\n" +
                    "            )  _.\". \"     .  \";\n" +
                    "          .'_.'   .'   '  __.,`.\n" +
                    "         '\"      \"\"''---'`    \"'```";
            case 4: return "```" +
                    "  \n" +
                    "              ,-\"\"-.\n" +
                    "             /  _  _\\\n" +
                    "            /  /_; ;_\\\n" +
                    "           / ,-.o) (o|\n" +
                    "          / (  =.`-o' )=\n" +
                    "         (  \\`-. __J.'\n" +
                    "          )  )/   ( \n" +
                    "         /_,'/   , \\\n" +
                    "            /     \\ \\\n" +
                    "        ,  /    -,/\\ \\\n" +
                    "      ,{ \\/       `\\`\"'\n" +
                    "      { (        __/__\n" +
                    "      `{ /\\_________)))```";
            case 5: return "```" +
                    "    /| |\\\n" +
                    "   ( |-| )\n" +
                    "    ) \" (\n" +
                    "   (>(Y)<)\n" +
                    "    )   (\n" +
                    "   /     \\\n" +
                    "  ( (m|m) )  \n" +
                    ",-.),___.(,-.`\n" +
                    "`---'   `---'```";
            default: return "";
        }
    }
}
