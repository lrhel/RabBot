package com.github.lrhel.rabbot.command.pokemon;

import com.github.lrhel.rabbot.utility.ExtendedBoolean;
import com.vdurmont.emoji.EmojiParser;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.Pokemon;
import me.sargunvohra.lib.pokekotlin.model.PokemonSpeciesFlavorText;
import org.discordbots.api.client.DiscordBotListAPI;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.event.ListenerManager;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.github.lrhel.rabbot.command.pokemon.RabbotPokemon.addPokemon;
import static com.github.lrhel.rabbot.command.pokemon.RabbotPokemon.getTimestamp;
import static com.github.lrhel.rabbot.utility.Utility.firstUpper;
import static com.github.lrhel.rabbot.utility.Utility.getMessageDeleter;


public class PokemonCommand implements CommandExecutor {
    public static int TOTAL_PKMN = 802;
    private static int INTERVAL = 30 * 1000;
    private static int SHINY_RATE = 4096;

    private static ArrayList<User> playing = new ArrayList<>(10);

    private DiscordBotListAPI discordBotListAPI;

    public PokemonCommand(DiscordBotListAPI dbl) {
        this.discordBotListAPI = dbl;
    }

    @Command(aliases = {"pokemon", "pkmn"}, description = "Catch a Pokemon", async = true)
    public void onPokemonCommand(User user, TextChannel textChannel){
        PokeApi pokeApi = new PokeApiClient();
        Random rng = new Random(System.currentTimeMillis());
        Pokemon pokemon;
        String url = "https://discord.gg/N5c9zfP";
        String name;
        int now = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
        ExtendedBoolean shiny = new ExtendedBoolean(false);

        if(playing.contains(user)) {
            return;
        }

        playing.add(user);

        int timestamp = getTimestamp(user);

        if ((timestamp + INTERVAL) > now && (timestamp - now + INTERVAL) <= INTERVAL) {
            int second = (timestamp - now + INTERVAL) / 1000;
            textChannel.sendMessage("Try again in " + second + " seconds").thenAccept(getMessageDeleter(5, TimeUnit.SECONDS));
            playing.remove(user);
            return ;
        }

        try {
            pokemon = pokeApi.getPokemon(rng.nextInt(TOTAL_PKMN) + 1);
        } catch (Exception e) {
            textChannel.sendMessage("Sorry... The PokeAPI is down...").thenAccept(getMessageDeleter(5, TimeUnit.SECONDS));
            playing.remove(user);
            return ;
        }

        if(rng.nextInt(SHINY_RATE) == 444) {
            shiny.set(true);
        } else {
            discordBotListAPI.hasVoted(user.getIdAsString()).whenComplete((aBoolean, throwable) -> {
                if (aBoolean.booleanValue()) {
                    if (rng.nextInt(SHINY_RATE) >= 4000) {
                        shiny.set(true);
                    }
                }
            }).toCompletableFuture().join();
        }

        name = shiny.is() ? "Shiny " + firstUpper(pokemon.getName()) : firstUpper(pokemon.getName());

        try {
            addPokemon(user, pokemon, shiny.is());

        } catch (Exception e) { e.printStackTrace(); }

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
                .setAuthor("Congratulation " + user.getName() + "!!", url, "https://image.noelshack.com/fichiers/2018/29/1/1531772093-pokeball.png");
        if (shiny.is()) {
            catchPokemonEmbed.setThumbnail(pokemon.getSprites().getFrontShiny());
        } else {
            catchPokemonEmbed.setThumbnail(pokemon.getSprites().getFrontDefault());
        }

        EmbedBuilder pokedexEntryEmbed = new EmbedBuilder()
                .setTitle("Pokedex entry for " + name)
                .setDescription(flavour)
                .setAuthor("Congratulation " + user.getName() + "!!", url, "https://image.noelshack.com/fichiers/2018/29/1/1531772093-pokeball.png");

        if(shiny.is()) {
            pokedexEntryEmbed.setThumbnail(pokemon.getSprites().getFrontShiny());
        } else {
            pokedexEntryEmbed.setThumbnail(pokemon.getSprites().getFrontDefault());
        }

        EmbedBuilder finalEmbed = new EmbedBuilder()
                .setTitle(name)
                .setDescription("")
                .setAuthor("Congratulation " + user.getName() + "!!", url, "https://image.noelshack.com/fichiers/2018/29/1/1531772093-pokeball.png");

        if (shiny.is()) {
            finalEmbed.setThumbnail(pokemon.getSprites().getFrontShiny());
        } else {
            finalEmbed.setThumbnail(pokemon.getSprites().getFrontDefault());
        }

        Message message = textChannel.sendMessage(catchPokemonEmbed).join();
        try {
            message.addReaction(EmojiParser.parseToUnicode(":arrow_right:"));

            lm.set(message.addReactionAddListener(e -> {
                if (e.getUser().getId() == user.getId() && e.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":arrow_right:"))) {
                    message.edit(pokedexEntryEmbed);
                    lm.get().remove();
                }
            }).removeAfter(5, TimeUnit.MINUTES));

            lm2.set(message.addReactionRemoveListener(e -> {
                if (e.getUser().getId() == user.getId() && e.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":arrow_right:"))) {
                    message.edit(finalEmbed);
                    try {
                        message.removeAllReactions().join();
                    } catch (Exception ignored) { }
                    lm2.get().remove();
                }
            }).removeAfter(5, TimeUnit.MINUTES));
        } catch (Exception ignored) { }
        playing.remove(user);
    }
}
