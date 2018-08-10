package com.github.lrhel.rabbot.command.pokemon;

import com.github.lrhel.rabbot.sqlite.Sqlite;
import me.sargunvohra.lib.pokekotlin.model.Pokemon;
import org.javacord.api.entity.user.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.github.lrhel.rabbot.sqlite.Sqlite.getTimestampFromSql;
import static com.github.lrhel.rabbot.utility.Utility.firstUpper;

public interface RabbotPokemon {
    static void addPokemon(User user, Pokemon pokemon, boolean shiny) throws SQLException {
        String sql = "INSERT INTO CATCH(DISCORD_ID, PKMN, PKMN_NAME, TIMESTAMP) " +
                "VALUES(?,?,?,?)";
        StringBuilder pokemonName = new StringBuilder();

        if (shiny) {
            pokemonName.append("Shiny ");
        }
        pokemonName.append(firstUpper(pokemon.getName()));

        PreparedStatement pstmt = Sqlite.getInstance().getConnection().prepareStatement(sql);
        pstmt.setString(1, user.getIdAsString());
        pstmt.setInt(2, pokemon.getId());
        pstmt.setString(3, pokemonName.toString());
        pstmt.setInt(4, Math.toIntExact(System.currentTimeMillis() % Integer.MAX_VALUE));
        pstmt.executeUpdate();
        pstmt.close();
    }

    static void addPokemon(User user, Pokemon pokemon) throws SQLException {
        addPokemon(user, pokemon, false);
    }


    static int totalCatchedPokemon(User user) throws  SQLException {
        String sql  = "SELECT COUNT(*) as count FROM catch WHERE discord_id = ?";
        PreparedStatement preparedStatement = Sqlite.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, user.getIdAsString());
        return preparedStatement.executeQuery().getInt("count");
    }

    static int totalCatchedPokemon() throws SQLException {
        String sql  = "SELECT COUNT(*) as count FROM catch";
        PreparedStatement preparedStatement = Sqlite.getInstance().getConnection().prepareStatement(sql);
        return preparedStatement.executeQuery().getInt("count");
    }

    static int totalUniqueCatchedPokemon() throws SQLException {
        String sql = "SELECT COUNT(DISTINCT PKMN_NAME) as count FROM catch";
        PreparedStatement preparedStatement = Sqlite.getInstance().getConnection().prepareStatement(sql);
        return preparedStatement.executeQuery().getInt("count");
    }

    static int totalUniqueCatchedPokemon(User user) throws SQLException {
        String sql  = "SELECT COUNT(DISTINCT PKMN_NAME) as count FROM catch WHERE discord_id = ?";
        PreparedStatement preparedStatement = Sqlite.getInstance().getConnection().prepareStatement(sql);
        preparedStatement.setString(1, user.getIdAsString());
        return preparedStatement.executeQuery().getInt("count");
    }

    /*
     * Get the timestamp of the last time the given user used the pokemon command
     * @param user
     * @return
     */
    static int getTimestamp(User user) {
        String sql = "SELECT * FROM catch WHERE discord_id = ? ORDER BY id DESC LIMIT 0, 1";
        return getTimestampFromSql(user,  sql);
    }

}
