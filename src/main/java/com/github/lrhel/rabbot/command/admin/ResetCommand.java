package com.github.lrhel.rabbot.command.admin;

import java.util.ArrayList;

import de.kaleidox.javacord.util.commands.Command;

import com.github.lrhel.rabbot.sqlite.Sqlite;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;

public class ResetCommand {
    @Command
    public String reset(User user, String[] args, Message message) {
        if (user.isBotOwner()) {
            if (args.length == 0) {
                return "What do you wanna reset? [catch|daily|bonus]";
            }
            ArrayList<User> mention = new ArrayList<>(message.getMentionedUsers());
            StringBuilder sb = new StringBuilder();
            switch (args[0].toLowerCase()) {
                case "catch":
                    if (mention.size() == 0) {
                        if (Sqlite.resetCatchTimestamp()) {
                            return "Catch time has been reset";
                        } else {
                            return "Error occurred";
                        }
                    } else {
                        for (User users : mention) {
                            if (Sqlite.resetCatchTimestamp(users)) {
                                sb.append(users.getName())
                                        .append(" got his catch time reset\n");
                            } else {
                                sb.append(users.getName())
                                        .append(": error occurred. . .\n");
                            }
                        }
                    }
                    break;
                case "daily":
                    if (mention.size() == 0) {
                        if (Sqlite.resetDailyTimestamp()) {
                            return "Daily time has been reset";
                        } else {
                            return "Error occurred";
                        }
                    } else {
                        for (User users : mention) {
                            if (Sqlite.resetDailyTimestamp(users)) {
                                sb.append(users.getName())
                                        .append(" got his daily time reset\n");
                            } else {
                                sb.append(users.getName())
                                        .append(": error occurred. . .\n");
                            }
                        }
                    }
                    break;
                case "bonus":
                    if (mention.size() == 0) {
                        if (Sqlite.resetBonusTimestamp()) {
                            return "Bonus time has been reset";
                        } else {
                            return "Error occurred";
                        }
                    } else {
                        for (User users : mention) {
                            if (Sqlite.resetBonusTimestamp(users)) {
                                sb.append(users.getName())
                                        .append(" got his bonus time reset\n");
                            } else {
                                sb.append(users.getName())
                                        .append(": error occurred. . .\n");
                            }
                        }
                    }
                    break;
                default:
                    return "What do you wanna reset? [catch|daily|bonus]";

            }
            return sb.toString();
        }
        return "";
    }
}