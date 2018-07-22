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
import java.util.concurrent.TimeUnit;

public class InventoryCommand implements CommandExecutor {
    @Command(aliases = {"inventory", "inv"}, description = "Pokemon inventory")
    public String onCommand(User user, TextChannel textChannel){
        try {
            final String sql = "SELECT PKMN_NAME, COUNT(*) as NB FROM CATCH WHERE DISCORD_ID = ? GROUP BY PKMN_NAME LIMIT ? OFFSET ?";
            final int limit = 20;
            String result = "";
            Offset offset = new Offset();

            Connection c = Sqlite.getInstance().getConnection();
            PreparedStatement pstmt = c.prepareStatement(sql);
            pstmt.setString(1, user.getIdAsString());
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset.getOffset());
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                result += rs.getString("PKMN_NAME") + " x" + rs.getInt("NB") + "\n";
            }
            Message msg = textChannel.sendMessage(result).get();
            msg.addReaction(EmojiParser.parseToUnicode(":arrow_left:")); //left arrow
            msg.addReaction(EmojiParser.parseToUnicode(":arrow_right:")); //right arrow
            msg.addReaction(EmojiParser.parseToUnicode(":x:"));//X cross❌❌ ❌
            msg.addReactionAddListener(event -> {
                try {
                    String result1 = "";
                    PreparedStatement pstmt1;
                    if (event.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":arrow_left:")) && event.getUser().getId() == user.getId()) { //left arrow
                        if (offset.getOffset() != 0) {
                            offset.minusOffset(limit);
                            pstmt1 = c.prepareStatement(sql);
                            pstmt1.setString(1, user.getIdAsString());
                            pstmt1.setInt(2, limit);
                            pstmt1.setInt(3, offset.getOffset());
                            ResultSet rs1 = pstmt1.executeQuery();
                            result1 = "";
                            while (rs1.next()) {
                                result1 += rs1.getString("PKMN_NAME") + " x" + rs1.getInt("NB") + "\n";
                            }
                            msg.edit(result1);
                        }
                    }
                    if (event.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":arrow_right:")) && event.getUser().getId() == user.getId()) { //right arrow
                        offset.plusOffset(limit);
                        pstmt1 = c.prepareStatement(sql);
                        pstmt1.setString(1, user.getIdAsString());
                        pstmt1.setInt(2, limit);
                        pstmt1.setInt(3, offset.getOffset());
                        ResultSet rs1 = pstmt1.executeQuery();
                        result1 = "";
                        while (rs1.next()) {
                            result1 += rs1.getString("PKMN_NAME") + " x" + rs1.getInt("NB") + "\n";
                        }
                        if(!result1.contentEquals(""))
                            msg.edit(result1);
                        else
                            offset.minusOffset(limit);
                    }
                    if (event.getEmoji().equalsEmoji(EmojiParser.parseToUnicode(":x:")) && event.getUser().getId() == user.getId()) { //X Cross
                        msg.delete();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }).removeAfter(3, TimeUnit.MINUTES);


        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private class Offset{
        private int offset;

        public Offset(){
            this.offset = 0;
        }

        public int getOffset() {
            return offset;
        }

        public void plusOffset(int limit) {
            this.offset += limit;
        }

        public void minusOffset(int limit) {
            this.offset -= limit;
        }
    }
}
