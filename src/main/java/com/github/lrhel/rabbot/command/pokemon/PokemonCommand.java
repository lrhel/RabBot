package com.github.lrhel.rabbot.command.pokemon;

import com.github.lrhel.rabbot.sqlite.Sqlite;
import com.vdurmont.emoji.EmojiParser;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.*;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.event.ListenerManager;

import java.sql.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


public class PokemonCommand implements CommandExecutor {
    private static int TOTAL_PKMN = 802;
    private static int INTERVAL = 30 * 1000;

    @Command(aliases = {"pokemon", "pkmn"}, description = "Catch a Pokemon", async = true)
    public String onCommand(User user, TextChannel textChannel){
        PokeApi pokeApi = new PokeApiClient();
        Random rng = new Random(System.currentTimeMillis());
        Pokemon pokemon;
        String url = "https://discord.gg/g88Wrfa";

        try {
            pokemon = pokeApi.getPokemon(rng.nextInt(TOTAL_PKMN) + 1);
        } catch (Exception e) {
            return "Sorry... The PokeAPI is down...";
        }
        String name1 = pokemon.getName();
        String sql = null;

        final String name = name1.substring(0,1).toUpperCase() + name1.substring(1);

        try {
            Connection c = Sqlite.getInstance().getConnection();

            sql = "SELECT TIMESTAMP FROM CATCH WHERE DISCORD_ID = ? ORDER BY ID DESC LIMIT 1";

            PreparedStatement slct = c.prepareStatement(sql);
            slct.setString(1, user.getIdAsString());

            ResultSet rs = slct.executeQuery();

            while(rs.next()) {
                int ts = rs.getInt("TIMESTAMP");
                if((ts + INTERVAL )> Math.toIntExact(System.currentTimeMillis() % Integer.MAX_VALUE)) {
                    return "Try again in " + ((ts - Math.toIntExact(System.currentTimeMillis() % Integer.MAX_VALUE) + INTERVAL) / 1000) + " seconds";
                }
            }
            sql = "INSERT INTO CATCH(DISCORD_ID, PKMN, PKMN_NAME, TIMESTAMP) " +
                    "VALUES(?,?,?,?)";
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, user.getIdAsString());
            pstmt.setInt(2, pokemon.getId());
            pstmt.setString(3, name);
            pstmt.setInt(4, Math.toIntExact(System.currentTimeMillis() % Integer.MAX_VALUE));
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        String flavour = "";
        List<PokemonSpeciesFlavorText> flavourText = pokeApi.getPokemonSpecies(pokemon.getId()).getFlavorTextEntries();
        for(PokemonSpeciesFlavorText ft : flavourText) {
            if(ft.getLanguage().component1().equalsIgnoreCase("en"))
                flavour = ft.getFlavorText();
        }

        AtomicReference<ListenerManager> lm = new AtomicReference<>();
        AtomicReference<ListenerManager> lm2 = new AtomicReference<>();

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

        return "";
    }

}
