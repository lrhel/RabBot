package com.github.lrhel.rabbot.command.pokemon;

import com.github.lrhel.rabbot.sqlite.Sqlite;
import com.vdurmont.emoji.EmojiParser;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class InventoryCommand implements CommandExecutor {
    @Command(aliases = {"inventory", "inv"}, description = "Pokemon inventory")
    public String onCommand(User user, TextChannel textChannel){
        try {
            final String sql = "SELECT PKMN, PKMN_NAME, COUNT(*) as NB FROM CATCH WHERE DISCORD_ID = ? GROUP BY PKMN_NAME ORDER BY PKMN LIMIT ? OFFSET ?";
            final int limit = 20;
            StringBuilder result = new StringBuilder();
            Offset offset = new Offset();

            result.append("```css\n");

            Connection c = Sqlite.getInstance().getConnection();
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, user.getIdAsString());
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset.getOffset());
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                result.append("#");
                result.append(rs.getString("PKMN"));
                result.append(Integer.parseInt(rs.getString("PKMN")) < 100 ? " " : "");
                result.append(Integer.parseInt(rs.getString("PKMN")) < 10 ? " " : "");
                result.append(" ");
                result.append(rs.getString("PKMN_NAME"));
                result.append(" x");
                result.append(rs.getInt("NB"));
                result.append("\n");
            }
            result.append("```");
            Message msg = textChannel.sendMessage(result.toString()).get();
            msg.addReaction(EmojiParser.parseToUnicode(":arrow_left:")); //left arrow
            msg.addReaction(EmojiParser.parseToUnicode(":arrow_right:")); //right arrow
            msg.addReaction(EmojiParser.parseToUnicode(":x:"));//X cross❌❌ ❌
            msg.addReactionAddListener(event -> {
                try {
                    StringBuilder result1 = new StringBuilder();
                    PreparedStatement pstmt1;
                    if (event.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":arrow_left:")) && event.getUser().getId() == user.getId()) { //left arrow
                        if (offset.getOffset() != 0) {
                            offset.minusOffset(limit);
                            pstmt1 = c.prepareStatement(sql);
                            pstmt1.setString(1, user.getIdAsString());
                            pstmt1.setInt(2, limit);
                            pstmt1.setInt(3, offset.getOffset());
                            ResultSet rs1 = pstmt1.executeQuery();
                            result1.append("```css\n");
                            getResult(rs1, result1);
                            result1.append("```");
                            msg.edit(result1.toString());
                        }
                    }
                    if (event.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":arrow_right:")) && event.getUser().getId() == user.getId()) { //right arrow
                        offset.plusOffset(limit);
                        pstmt1 = c.prepareStatement(sql);
                        pstmt1.setString(1, user.getIdAsString());
                        pstmt1.setInt(2, limit);
                        pstmt1.setInt(3, offset.getOffset());
                        ResultSet rs1 = pstmt1.executeQuery();
                        result1.append("```css\n");
                        getResult(rs1, result1);
                        if(!result1.toString().contentEquals("```css\n")) {
                            result1.append("```");
                            msg.edit(result1.toString());
                        }
                        else
                            offset.minusOffset(limit);
                    }
                    if (event.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":x:")) && event.getUser().getId() == user.getId()) { //X Cross
                        msg.delete();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }).removeAfter(5, TimeUnit.MINUTES);


        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    void getResult(ResultSet rs1, StringBuilder result1) throws SQLException {
        while (rs1.next()) {
            result1.append("#");
            result1.append(rs1.getString("PKMN"));
            result1.append(" ");
            result1.append(rs1.getString("PKMN_NAME"));
            result1.append(" x");
            result1.append(rs1.getInt("NB"));
            result1.append("\n");
        }
    }

    private class Offset{
        private int offset;

        Offset(){
            this.offset = 0;
        }

        int getOffset() {
            return offset;
        }

        void plusOffset(int limit) {
            this.offset += limit;
        }

        void minusOffset(int limit) {
            this.offset -= limit;
        }
    }
}
