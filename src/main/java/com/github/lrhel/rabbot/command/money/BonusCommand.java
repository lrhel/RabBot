package com.github.lrhel.rabbot.command.money;

import com.github.lrhel.rabbot.Money;
import com.github.lrhel.rabbot.command.pokemon.RabbotPokemon;
import com.github.lrhel.rabbot.sqlite.Sqlite;
import com.github.lrhel.rabbot.utility.Utility;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.Pokemon;
import org.discordbots.api.client.DiscordBotListAPI;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.github.lrhel.rabbot.command.pokemon.PokemonCommand.TOTAL_PKMN;
import static com.github.lrhel.rabbot.utility.Utility.firstUpper;

public class BonusCommand implements CommandExecutor {
    private DiscordBotListAPI discordBotListAPI;
    private static int INTERVAL = 4 * 60 * 60 * 1000;

    private static ArrayList<User> using = new ArrayList<>(10);

    public BonusCommand(DiscordBotListAPI dblapi) {
        this.discordBotListAPI = dblapi;
    }

    @Command(aliases = {"bonus"}, description = "Bonus if you voted the Bot", async = true)
    public void onBonusCommand(User user, TextChannel textChannel, DiscordApi api) {
        if (user.isBot()) { return ; }

        int timestamp = Money.getBonusTimestamp(user);
        int now = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);

        if(using.contains(user)) {
            return;
        }

        using.add(user);

        discordBotListAPI.hasVoted(user.getIdAsString()).whenComplete((aBoolean, throwable) -> {
            EmbedBuilder embedBuilder;
            //has voted
            if (aBoolean) {
                Pokemon pokemon;
                RabbotPokemon rabbotPokemon;
                PokeApi pokeApi = new PokeApiClient();

                if ((timestamp + INTERVAL) > now && (timestamp - now + INTERVAL) <= INTERVAL) {
                    int second = (timestamp - now + INTERVAL) / 1000;
                    textChannel.sendMessage("Try again in " + (second / 3600) + " hours and " + ((second / 60) % 60) + " minutes").thenAccept(Utility.getMessageDeleter(5, TimeUnit.SECONDS));
                    using.remove(user);
                    return;
                }

                Random rng = new Random(System.currentTimeMillis());
                StringBuilder stringBuilder = new StringBuilder();
                int pokemonId = rng.nextInt(TOTAL_PKMN) + 1;
                stringBuilder.append("You got **20.000$** ");
                Money.addBonus(user, 20000, now);
                embedBuilder = new EmbedBuilder();

                rabbotPokemon = RabbotPokemon.getPokemon(pokemonId);
                if (rabbotPokemon == null) {
                    try {
                        pokemon = pokeApi.getPokemon(pokemonId);
                        stringBuilder.append("and a Shiny ").append(firstUpper(pokemon.getName()));
                        RabbotPokemon.addPokemon(user, pokemon, true);
                        embedBuilder.setThumbnail(pokemon.getSprites().getFrontShiny());
                    } catch (Exception e) { }
                }
                else {
                    stringBuilder.append("and a Shiny ").append(firstUpper(rabbotPokemon.getPokemonName()));
                    try {
                        RabbotPokemon.addPokemon(user, rabbotPokemon, true);
                    } catch (Exception e) { }
                    embedBuilder.setThumbnail(rabbotPokemon.getImageShiny());
                }
                embedBuilder.setAuthor("RabBot's Bonus", "https://discordbots.org/bot/441010449757110273", api.getYourself().getAvatar())
                        .addField("Congratulations" +
                                "", stringBuilder.toString());

            }
            //has not voted
            else {
                Sqlite.resetBonusTimestamp(user);
                embedBuilder = new EmbedBuilder()
                        .setAuthor("RabBot's Bonus", "https://discordbots.org/bot/441010449757110273", api.getYourself().getAvatar())
                        .addField("Unlock the daily Bonus", "[Click here to vote for RabBot and unlock the daily Bonus](https://discordbots.org/bot/441010449757110273/vote)")
                        .setFooter("Also don't forget to re-run the command after voting and 1 or 2 minutes (o.o")
                ;
            }

            textChannel.sendMessage(embedBuilder.setColor(Color.BLUE));
            using.remove(user);

        });

    }
}
