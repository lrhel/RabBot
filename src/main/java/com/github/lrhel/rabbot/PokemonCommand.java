package com.github.lrhel.rabbot;

import com.github.lrhel.rabbot.sqlite.Sqlite;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.*;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.sql.*;
import java.util.Random;


public class PokemonCommand implements CommandExecutor {
    private static int TOTAL_PKMN = 802;
    private static int INTERVAL = 30 * 1000;

    @Command(aliases = {"pokemon", "pkmn"}, description = "Catch a Pokemon", async = true)
    public String onCommand(User user, TextChannel textChannel){
        PokeApi pokeApi = new PokeApiClient();
        Random rng = new Random(System.currentTimeMillis());
        Pokemon pokemon;
        try {
            pokemon = pokeApi.getPokemon(rng.nextInt(TOTAL_PKMN) + 1);
        } catch (Exception e) {
            return "Sorry... The PokeAPI is down...";
        }
        String name = pokemon.getName();
        String sql = null;

        name = name.substring(0,1).toUpperCase() + name.substring(1);

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

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("You've caught a Pokemon!!")
                .setDescription("You've caught a " + name)
                .setAuthor("Congratulation " + user.getName() + "!!", "https://discord.gg/T67RCeA", "https://image.noelshack.com/fichiers/2018/29/1/1531772093-pokeball.png")
                .setThumbnail(pokemon.getSprites().getFrontDefault());
        textChannel.sendMessage(embed);
        return "";
    }

}
