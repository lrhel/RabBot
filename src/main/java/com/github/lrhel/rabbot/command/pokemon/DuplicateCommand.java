package com.github.lrhel.rabbot.command.pokemon;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.kaleidox.javacord.util.commands.Command;

import com.github.lrhel.rabbot.utility.Offset;
import com.vdurmont.emoji.EmojiParser;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

public class DuplicateCommand {
    private static int LIMIT = 20;

    @Command(aliases = {"duplication", "duplicate", "dup"})
    public void onDuplicateCommand(User user, TextChannel textChannel) {
        try {
            LinkedHashMap<RabbotPokemon, Integer> map = RabbotPokemon.getDoublePokemonFromUser(user);
            Offset offset = new Offset();
            Message msg = textChannel.sendMessage(getDuplicateOffset(map, offset)).join();
            msg.addReaction(EmojiParser.parseToUnicode(":arrow_left:")); //left arrow
            msg.addReaction(EmojiParser.parseToUnicode(":arrow_right:")); //right arrow
            msg.addReaction(EmojiParser.parseToUnicode(":x:"));//X cross

            msg.addReactionAddListener(event -> {
                if (event.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":arrow_left:")) && event.getUser().getId() == user.getId()) { //left arrow
                    if (offset.getOffset() != 0) {
                        offset.minusOffset(LIMIT);
                        msg.edit(getDuplicateOffset(map, offset)).join();
                    }
                }
                if (event.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":arrow_right:")) && event.getUser().getId() == user.getId()) { //right arrow
                    offset.plusOffset(LIMIT);
                    msg.edit(getDuplicateOffset(map, offset)).join();
                }
                if (event.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":x:")) && event.getUser().getId() == user.getId()) { //X Cross
                    msg.delete();
                }
            }).removeAfter(5, TimeUnit.MINUTES);
        } catch (Exception e) {
        }
    }

    private static String getDuplicateOffset(LinkedHashMap<RabbotPokemon, Integer> pokemonMap, Offset offset) {
        StringBuilder sb = new StringBuilder();
        if (offset.getOffset() >= pokemonMap.size()) {
            offset.minusOffset(LIMIT);
        }
        int i = 0;
        Iterator<Map.Entry<RabbotPokemon, Integer>> iterator = pokemonMap.entrySet().iterator();
        while (iterator.hasNext()) {
            if (i == offset.getOffset()) {
                break;
            }
            iterator.next();
            i++;
        }
        while (iterator.hasNext() && i < offset.getOffset() + LIMIT) {
            Map.Entry<RabbotPokemon, Integer> next = iterator.next();
            sb.append(next.getKey().getPokemonName()).append(" x")
                    .append(next.getValue().intValue()).append("\n");
            i++;
        }
        return sb.toString();
    }
}
