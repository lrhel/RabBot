package com.github.lrhel.rabbot.command.admin;

import de.kaleidox.javacord.util.commands.Command;

import com.github.lrhel.rabbot.command.pokemon.RabbotPokemon;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

public class AddPokemonCommand {
    @Command(aliases = {"add_pkmn", "addpkmn"})
    public String onAddPokemonCommand(User user, String[] args, Message message) {
        if (user.isBotOwner()) {
            if (args.length < 2) {
                return "rb.add_pkmn pkmn_number user";
            }
            int pkmn_number;
            try {
                pkmn_number = Integer.parseInt(args[0]);
            } catch (Exception e) {
                return "bad id";
            }
            RabbotPokemon pokemon = RabbotPokemon.getPokemon(pkmn_number);
            if (pokemon == null) {
                return "not yet cached";
            }
            StringBuilder sb = new StringBuilder();
            for (User users : message.getMentionedUsers()) {
                try {
                    RabbotPokemon.addPokemon(users, pokemon, true);
                    sb.append("Shiny ").append(pokemon.getPokemonName()).append(" added to ").append(users.getName());
                } catch (Exception e) {
                    return "error with " + users.getId();
                }
            }
            return sb.toString();
        }
        return "";
    }
}
