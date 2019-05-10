package com.github.lrhel.rabbot.command.misc;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import de.kaleidox.javacord.util.commands.Command;

import com.samuelmaddock.strawpollwrapper.StrawPoll;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.event.ListenerManager;

public class StrawpollCommand {
    private static ArrayList<User> using = new ArrayList<>();

    @Command(aliases = {"strawpoll", "sp"}, enablePrivateChat = false, description = "Make a strawpoll!", shownInHelpCommand = false)
    public String onStrawpollCommand(User user, String[] args, TextChannel textChannel) {
        if (user.isBot()) {
            return "";
        }

        if (args.length == 0) {
            return showHelpMessage();
        }
        if (using.contains(user)) {
            return "";
        }

        switch (args[0].toLowerCase()) {
            case "new":
            case "add":
                strawpoll(user, textChannel);
                break;
            case "check":
            case "get":
                check(user, textChannel, args);
                break;
            default:
                using.remove(user);
                return showHelpMessage();
        }
        using.remove(user);

        return "";
    }

    private String showHelpMessage() {
        return "**Usage:** ```rb.strawpoll [new | check [id]]```";
    }

    private String showHelpMessageCheck() {
        return "**Usage:** ```rb.strawpoll [check] [id]```";
    }


    private void strawpoll(User user, TextChannel textChannel) {
        AtomicReference<String> title = new AtomicReference<>();
        AtomicReference<ArrayList<String>> option = new AtomicReference<>(new ArrayList<>(3));
        AtomicReference<Boolean> waiting = new AtomicReference<>(true);
        AtomicReference<ListenerManager> listenerManager = new AtomicReference<>();
        AtomicReference<Boolean> breaking = new AtomicReference<>(false);
        StrawPoll strawPoll;
        StringBuilder response = new StringBuilder();

        textChannel.sendMessage("The title of the Strawpoll?").join();
        listenerManager.set(textChannel.addMessageCreateListener(messageCreateEvent -> {
            Message message = messageCreateEvent.getMessage();
            if (message.getUserAuthor().isPresent() && message.getUserAuthor().get().getId() == user.getId()) {
                title.set(message.getContent());
                waiting.set(false);
                listenerManager.get().remove();
            }
        }).removeAfter(3, TimeUnit.MINUTES));
        listenerManager.get().addRemoveHandler(() -> waiting.set(false));

        while (waiting.get()) {
            Thread.onSpinWait();
        }

        if (title.get() == null) {
            textChannel.sendMessage("No StrawPoll has been generated").join();
            return;
        }

        //Option part
        textChannel.sendMessage("Now we will handle the options, say `STOP` if you don't want to add any more options");
        for (int i = 1; i <= 50; i++) {
            AtomicReference<Boolean> forWaiting = new AtomicReference<>(true);
            textChannel.sendMessage("Option #" + i + " ?").join();
            listenerManager.set(textChannel.addMessageCreateListener(messageCreateEvent -> {
                Message message = messageCreateEvent.getMessage();
                if (message.getUserAuthor().isPresent() && message.getUserAuthor().get().getId() == user.getId()) {
                    if (message.getContent().equalsIgnoreCase("stop")) {
                        breaking.set(true);
                    } else {
                        option.get().add(message.getContent());
                    }
                    forWaiting.set(false);
                    listenerManager.get().remove();
                }
            }).removeAfter(3, TimeUnit.SECONDS));
            listenerManager.get().addRemoveHandler(() -> forWaiting.set(false));

            while (forWaiting.get()) {
                Thread.onSpinWait();
            }

            try {
                option.get().get(i - 1);
            } catch (IndexOutOfBoundsException ignored) {
                breaking.set(true);
            }

            if (breaking.get()) {
                break;
            }

        }

        if (option.get().size() <= 1) {
            textChannel.sendMessage("No StrawPoll has been generated").join();
            return;
        }

        //Creating the Strawpoll part
        strawPoll = new StrawPoll(title.get(), option.get());
        strawPoll.create();

        response.append("Your StrawPoll has been generated\n")
                .append("\n")

                .append("**ID: **")
                .append(strawPoll.getId())
                .append("\n")

                .append("**Title: **")
                .append(strawPoll.getTitle())
                .append("\n")

                .append("**Options: **\n```")
        ;

        int i = 1;
        for (String options : strawPoll.getOptions()) {
            response.append("Option #")
                    .append(i)
                    .append(": ")
                    .append(options)
                    .append("\n")
            ;
            i++;
        }

        response.append("```")
                .append("**URL: **")
                .append(strawPoll.getPollURL())
                .append("\n")
        ;

        textChannel.sendMessage(response.toString());
    }

    private void check(User user, TextChannel textChannel, String[] args) {
        StrawPoll strawPoll;
        ArrayList<Integer> result;
        ArrayList<String> options;
        StringBuilder response = new StringBuilder();

        if (args.length == 2) {
            try {
                int id = Integer.parseInt(args[1]);
                strawPoll = new StrawPoll(id);
                options = new ArrayList<>(strawPoll.getOptions());
                result = new ArrayList<>(strawPoll.getVotes());

                response.append("**ID: **")
                        .append(strawPoll.getId())
                        .append("\n")

                        .append("**Title: **")
                        .append(strawPoll.getTitle())
                        .append("\n")

                        .append("**Total voters: **")
                        .append(result.stream().mapToInt(Integer::intValue).sum())
                        .append("\n")

                        .append("**Options: **\n```")
                ;

                for (int i = 0; i < options.size(); i++) {
                    double percent = ((double) result.get(i) / (double) result.stream().mapToInt(Integer::intValue).sum()) * 100.0;

                    response
                            .append(options.get(i))
                            .append(": ")
                    ;
                    for (int j = 32 - options.get(i).length(); j > 0; j--) {
                        response.append(" ");
                    }
                    response
                            .append(percent < 10 ? " " + percent : percent)
                            .append("% (")
                            .append(result.get(i))
                            .append(" voters)\n")
                    ;

                }

                response.append("```\n**URL:** `")
                        .append(strawPoll.getPollURL())
                        .append("`\n")
                ;

                textChannel.sendMessage(response.toString());

            } catch (NumberFormatException ignored) {
            }
        } else {
            textChannel.sendMessage(showHelpMessageCheck());
        }

    }
}
