package com.github.lrhel.rabbot.command.pokemon;

import com.vdurmont.emoji.EmojiParser;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.Pokemon;
import me.sargunvohra.lib.pokekotlin.model.PokemonSpeciesFlavorText;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.event.ListenerManager;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.lrhel.rabbot.command.pokemon.RabbotPokemon.addPokemon;
import static com.github.lrhel.rabbot.command.pokemon.RabbotPokemon.getTimestamp;
import static com.github.lrhel.rabbot.utility.Utility.firstUpper;
import static com.github.lrhel.rabbot.utility.Utility.getMessageDeleter;


public class PokemonCommand implements CommandExecutor {
    private static int TOTAL_PKMN = 802;
    private static int INTERVAL = 30 * 1000;

    @Command(aliases = {"pokemon", "pkmn"}, description = "Catch a Pokemon", async = true)
    public void onCommand(User user, TextChannel textChannel){
        PokeApi pokeApi = new PokeApiClient();
        Random rng = new Random(System.currentTimeMillis());
        Pokemon pokemon;
        String url = "https://discord.gg/g88Wrfa";
        String name;

        try {
            pokemon = pokeApi.getPokemon(rng.nextInt(TOTAL_PKMN) + 1);
        } catch (Exception e) {
            textChannel.sendMessage("Sorry... The PokeAPI is down...").thenAccept(getMessageDeleter(5, TimeUnit.SECONDS));
            return ;
        }

        name = firstUpper(pokemon.getName());

        try {
            int ts = getTimestamp(user);

            if((ts + INTERVAL )> Math.toIntExact(System.currentTimeMillis() % Integer.MAX_VALUE) && (ts - Math.toIntExact(System.currentTimeMillis() % Integer.MAX_VALUE) + INTERVAL) <= INTERVAL) {
                textChannel.sendMessage("Try again in " + ((ts - Math.toIntExact(System.currentTimeMillis() % Integer.MAX_VALUE) + INTERVAL) / 1000) + " seconds").thenAccept(getMessageDeleter(5, TimeUnit.SECONDS));
                return ;
            }

            addPokemon(user, pokemon);
        } catch (Exception ignored) { }

        String flavour = "";

        Optional<PokemonSpeciesFlavorText> flavourOptional = pokeApi.getPokemonSpecies(pokemon.getId()).getFlavorTextEntries().stream()
                .filter(f -> f.getLanguage().component1().equalsIgnoreCase("en"))
                .findAny();
        if (flavourOptional.isPresent()) {
            flavour = flavourOptional.get().getFlavorText();
        }

        AtomicReference<ListenerManager> lm = new AtomicReference<>();
        AtomicReference<ListenerManager> lm2 = new AtomicReference<>();

        //The different Embeds
        EmbedBuilder catchPokemonEmbed = new EmbedBuilder()
                .setTitle("You've caught a Pokemon!!")
                .setDescription("You've caught a " + name)
                .setAuthor("Congratulation " + user.getName() + "!!", url, "https://image.noelshack.com/fichiers/2018/29/1/1531772093-pokeball.png")
                .setThumbnail(pokemon.getSprites().getFrontDefault());

        EmbedBuilder pokedexEntryEmbed = new EmbedBuilder()
                .setTitle("Pokedex entry for " + name)
                .setDescription(flavour)
                .setAuthor("Congratulation " + user.getName() + "!!", url, "https://image.noelshack.com/fichiers/2018/29/1/1531772093-pokeball.png")
                .setThumbnail(pokemon.getSprites().getFrontDefault());

        EmbedBuilder finalEmbed = new EmbedBuilder()
                .setTitle(name)
                .setDescription("")
                .setAuthor("Congratulation " + user.getName() + "!!", url, "https://image.noelshack.com/fichiers/2018/29/1/1531772093-pokeball.png")
                .setThumbnail(pokemon.getSprites().getFrontDefault());

        Message message = textChannel.sendMessage(catchPokemonEmbed).join();
        message.addReaction(EmojiParser.parseToUnicode(":arrow_right:"));

        lm.set(message.addReactionAddListener(e -> {
            if (e.getUser().getId() == user.getId() && e.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":arrow_right:"))) {
                message.edit(pokedexEntryEmbed);
                lm.get().remove();
            }
        }).removeAfter(5, TimeUnit.MINUTES));

        lm2.set(message.addReactionRemoveListener(e -> {
            if(e.getUser().getId() == user.getId() && e.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":arrow_right:"))) {
                message.edit(finalEmbed);
                message.removeAllReactions().join();
                lm2.get().remove();
            }
        }).removeAfter(5, TimeUnit.MINUTES));
    }

}
