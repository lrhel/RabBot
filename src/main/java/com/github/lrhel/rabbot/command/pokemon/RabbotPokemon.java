package com.github.lrhel.rabbot.command.pokemon;

import com.github.lrhel.rabbot.sqlite.Sqlite;
import me.sargunvohra.lib.pokekotlin.client.PokeApi;
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient;
import me.sargunvohra.lib.pokekotlin.model.Pokemon;
import me.sargunvohra.lib.pokekotlin.model.PokemonSpeciesFlavorText;
import org.javacord.api.entity.user.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static com.github.lrhel.rabbot.sqlite.Sqlite.getTimestampFromSql;
import static com.github.lrhel.rabbot.utility.Utility.firstUpper;

public class RabbotPokemon {

    private int pokemonId;
    private String pokemonName;
    private String image;
    private String imageShiny;
    private String pokedex;

    private RabbotPokemon(int pokemonId, String pokemonName, String image, String imageShiny, String pokedex) {
        this.pokemonId = pokemonId;
        this.pokemonName = pokemonName;
        this.image = image;
        this.imageShiny = imageShiny;
        this.pokedex = pokedex;
    }

    public static RabbotPokemon getPokemon(int id) {
        String sql = "SELECT pkmn, pkmn_name, image, image_shiny, pokedex " +
                "FROM catch WHERE pkmn = ? AND pkmn_name NOT LIKE '%Shiny%'" +
                "AND image IS NOT NULL";
        try {
            PreparedStatement preparedStatement = Sqlite.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                String pkmnName = resultSet.getString("pkmn_name");
                String image = resultSet.getString("image");
                String imageShiny = resultSet.getString("image_shiny");
                String pokedex = resultSet.getString("pokedex");
                return new RabbotPokemon(id, pkmnName, image, imageShiny, pokedex);
            } else {
                return null;
            }
        } catch (Exception ignored) { ignored.printStackTrace();return null; }
    }

    public static void addPokemon(User user, Pokemon pokemon, boolean shiny) throws SQLException {
        String sql = "INSERT INTO CATCH(DISCORD_ID, PKMN, PKMN_NAME, TIMESTAMP, image, image_shiny, pokedex) " +
                "VALUES(?,?,?,?,?,?,?)";
        StringBuilder pokemonName = new StringBuilder();
        PokeApi pokeApi = new PokeApiClient();

        String flavour = "";

        Optional<PokemonSpeciesFlavorText> flavourOptional = pokeApi.getPokemonSpecies(pokemon.getId()).getFlavorTextEntries().stream()
                .filter(f -> f.getLanguage().component1().equalsIgnoreCase("en"))
                .findAny();
        if (flavourOptional.isPresent()) {
            flavour = flavourOptional.get().getFlavorText();
        }


        if (shiny) {
            pokemonName.append("Shiny ");
        }
        pokemonName.append(firstUpper(pokemon.getName()));

        PreparedStatement pstmt = Sqlite.getInstance().getConnection().prepareStatement(sql);
        pstmt.setString(1, user.getIdAsString());
        pstmt.setInt(2, pokemon.getId());
        pstmt.setString(3, pokemonName.toString());
        pstmt.setInt(4, Math.toIntExact(System.currentTimeMillis() % Integer.MAX_VALUE));
        pstmt.setString(5, pokemon.getSprites().getFrontDefault());
        pstmt.setString(6, pokemon.getSprites().getFrontShiny());
        pstmt.setString(7, flavour);
        pstmt.executeUpdate();
        pstmt.close();
    }

    public static void addPokemon(User user, RabbotPokemon pokemon, boolean shiny) throws SQLException {
        String sql = "INSERT INTO CATCH(DISCORD_ID, PKMN, PKMN_NAME, TIMESTAMP, image, image_shiny, pokedex) " +
                "VALUES(?,?,?,?,?,?,?)";
        StringBuilder pokemonName = new StringBuilder();

        if (shiny) {
            pokemonName.append("Shiny ");
        }
        pokemonName.append(firstUpper(pokemon.getPokemonName()));

        PreparedStatement pstmt = Sqlite.getInstance().getConnection().prepareStatement(sql);
        pstmt.setString(1, user.getIdAsString());
        pstmt.setInt(2, pokemon.getPokemonId());
        pstmt.setString(3, pokemonName.toString());
        pstmt.setInt(4, Math.toIntExact(System.currentTimeMillis() % Integer.MAX_VALUE));
        pstmt.setString(5, pokemon.getImage());
        pstmt.setString(6, pokemon.getImageShiny());
        pstmt.setString(7, pokemon.getPokedex());
        pstmt.executeUpdate();
        pstmt.close();
    }

    public static void addPokemon(User user, Pokemon pokemon) throws SQLException {
        addPokemon(user, pokemon, false);
    }

    public static int totalCatchedPokemon(User user) throws  SQLException {
        String sql  = "SELECT COUNT(*) as count FROM catch WHERE discord_id = ?";
        PreparedStatement preparedStatement = Sqlite.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, user.getIdAsString());
        return preparedStatement.executeQuery().getInt("count");
    }

    public static int totalCatchedPokemon() throws SQLException {
        String sql  = "SELECT COUNT(*) as count FROM catch";
        PreparedStatement preparedStatement = Sqlite.getInstance().getConnection().prepareStatement(sql);
        return preparedStatement.executeQuery().getInt("count");
    }

    public static int totalUniqueCatchedPokemon() throws SQLException {
        String sql = "SELECT COUNT(DISTINCT PKMN) as count FROM catch";
        PreparedStatement preparedStatement = Sqlite.getInstance().getConnection().prepareStatement(sql);
        return preparedStatement.executeQuery().getInt("count");
    }

    public static int totalUniqueCatchedPokemon(User user) throws SQLException {
        String sql  = "SELECT COUNT(DISTINCT PKMN) as count FROM catch WHERE discord_id = ?";
        PreparedStatement preparedStatement = Sqlite.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, user.getIdAsString());
        return preparedStatement.executeQuery().getInt("count");
    }

    public static int totalUniqueCatchedShinyPokemon() throws SQLException {
        String sql = "SELECT COUNT(DISTINCT PKMN) as count FROM catch WHERE pkmn_name LIKE 'Shiny%'";
        PreparedStatement preparedStatement = Sqlite.getInstance().getConnection().prepareStatement(sql);
        return preparedStatement.executeQuery().getInt("count");
    }

    public static int totalUniqueCatchedShinyPokemon(User user) throws SQLException {
        String sql  = "SELECT COUNT(DISTINCT PKMN) as count FROM catch WHERE discord_id = ? AND pkmn_name LIKE 'Shiny%'";
        PreparedStatement preparedStatement = Sqlite.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, user.getIdAsString());
        return preparedStatement.executeQuery().getInt("count");
    }




    public int getPokemonId() {
        return pokemonId;
    }

    public String getPokemonName() {
        return pokemonName;
    }

    public String getImage() {
        return image;
    }

    public String getImageShiny() {
        return imageShiny;
    }

    public String getPokedex() {
        return pokedex;
    }

    /*
     * Get the timestamp of the last time the given user used the pokemon command
     * @param user
     * @return
     */
    public static int getTimestamp(User user) {
        String sql = "SELECT * FROM catch WHERE discord_id = ? ORDER BY id DESC LIMIT 0, 1";
        return getTimestampFromSql(user,  sql);
    }

}
