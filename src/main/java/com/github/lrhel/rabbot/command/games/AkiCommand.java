package com.github.lrhel.rabbot.command.games;

import com.github.lrhel.rabbot.utility.ExtendedBoolean;
import com.markozajc.akiwrapper.Akiwrapper;
import com.markozajc.akiwrapper.AkiwrapperBuilder;
import com.markozajc.akiwrapper.core.entities.Guess;
import com.markozajc.akiwrapper.core.entities.Question;
import com.markozajc.akiwrapper.core.entities.Server;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.event.ListenerManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class AkiCommand implements CommandExecutor {
    private static ArrayList<User> using = new ArrayList<>();



    @Command(aliases = {"akinator", "aki"}, description = "Play with Akinator", async = true)
    public void onAkiCommand(User user, TextChannel textChannel, String[] arg, DiscordApi api, ServerTextChannel serverTextChannel) {
        AkiwrapperBuilder akiwrapperBuilder;
        Akiwrapper aki;

        ArrayList<String> guessList = new ArrayList<>();
        ExtendedBoolean next = new ExtendedBoolean(true);

        AtomicReference<Question> questionAtomicReference = new AtomicReference<>();
        AtomicReference<ListenerManager> listenerManagerAtomicReference = new AtomicReference<>();

        if(using.contains(user)){
            return;
        } else {
            using.add(user);
        }


        if(arg.length == 0) {
            akiwrapperBuilder = setLanguage("en");
        }
        else {
            String language = arg[0].toLowerCase();
            akiwrapperBuilder = setLanguage(language);
        }

        if(serverTextChannel.isNsfw()) {
            akiwrapperBuilder.setFilterProfanity(false);
        } else {
            akiwrapperBuilder.setFilterProfanity(true);
        }

        aki = akiwrapperBuilder.setName(user.getName()).build();

        questionAtomicReference.set(aki.getCurrentQuestion());

        /* GAME LOOP */
        while (next.is()) {
            textChannel.sendMessage(questionAtomicReference.get().getQuestion()).join();
            ExtendedBoolean answered = new ExtendedBoolean(false);
            listenerManagerAtomicReference.set(textChannel.addMessageCreateListener(event -> {
                if(event.getMessage().getUserAuthor().get().getId() == user.getId()) {
                    try {
                        String content = event.getMessage().getContent().toLowerCase();
                        if(content.equalsIgnoreCase("stop")) {
                            using.remove(user);
                            answered.set(true);
                            next.set(false);
                            return;
                        }
                        Akiwrapper.Answer answer = getAnswer(content);
                        if(answer != null) {
                            questionAtomicReference.set(aki.answerCurrentQuestion(answer));
                            answered.set(true);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        textChannel.sendMessage("Something went wrong... Rabbot is investigating");
                        listenerManagerAtomicReference.get().remove();
                        using.remove(user);
                        return;
                    }
                }
            }).removeAfter(30, TimeUnit.SECONDS));
            listenerManagerAtomicReference.get().addRemoveHandler(() -> answered.set(true));
            while (answered.isNot()) {
                Thread.onSpinWait();
            }

            // GUESSING BLOCK
            try {
                for (Guess guess : aki.getGuessesAboveProbability(0.80)) {

                    if (guessList.contains(guess.getName())) {
                        continue;
                    }

                    String guessName;
                    String guessDesc = null;
                    String guessImg = null;
                    try {
                        guessName = guess.getName();
                    } catch (Exception ignored) {
                        continue;
                    }
                    try {
                        guessDesc = guess.getDescription();
                    } catch (Exception ignored) {

                    }
                    try {
                        guessImg = guess.getImage().toString();
                    } catch (Exception ignored) {

                    }
                    EmbedBuilder embedBuilder = new EmbedBuilder();

                    if (guessName != null && !guessName.isEmpty()) {
                        embedBuilder.setAuthor(guess.getName() + " ?", "", "");
                    } else {
                        continue;
                    }
                    if (guessDesc != null && !guessDesc.isEmpty()) {
                        embedBuilder.addField("Description", guessDesc);
                    }
                    if (guessImg != null && !guessImg.isEmpty()) {
                        embedBuilder.setImage(guessImg);
                    }
                    textChannel.sendMessage(embedBuilder).join();
                    answered.set(false);


                    //Waiting for the answer
                    listenerManagerAtomicReference.set(textChannel.addMessageCreateListener(event -> {
                        if (event.getMessage().getUserAuthor().get().getId() == user.getId()) {
                            String content = event.getMessage().getContent().toLowerCase();
                            if (content.equalsIgnoreCase("yes")
                                    || content.equalsIgnoreCase("stop")
                                    || content.equalsIgnoreCase("no")
                                    || content.equalsIgnoreCase("oui")
                                    || content.equalsIgnoreCase("non")
                                    || content.equalsIgnoreCase("y")
                                    || content.equalsIgnoreCase("n")
                                    || content.equalsIgnoreCase("s")
                                    || content.equalsIgnoreCase("sim")
                                    || content.equalsIgnoreCase("si")
                                    || content.equalsIgnoreCase("nao")
                                    ) {
                                switch (content) {
                                    case "y":
                                    case "yes":
                                    case "oui":
                                    case "si":
                                    case "sim":
                                    case "s":
                                        textChannel.sendMessage("Akinator won!").join();
                                        next.set(false);
                                        answered.set(true);
                                        break;
                                    case "n":
                                    case "no":
                                    case "non":
                                    case "nao":
                                        guessList.add(guess.getName());
                                        answered.set(true);
                                        break;
                                    case "stop":
                                        next.set(false);
                                        answered.set(true);
                                        break;
                                }
                                listenerManagerAtomicReference.get().remove();
                            }

                        }
                    }).removeAfter(30, TimeUnit.SECONDS));
                    listenerManagerAtomicReference.get().addRemoveHandler(() -> answered.set(true));
                    while (answered.isNot()) {
                        Thread.onSpinWait();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                textChannel.sendMessage("Something went wrong...");
                using.remove(user);
                return;
            }
            //If the API doesn't have any question
            if(questionAtomicReference.get() == null) {
                // GUESSING BLOCK
                try {
                    for (Guess guess : aki.getGuesses()) {

                        if (guessList.contains(guess.getName())) {
                            continue;
                        }

                        String guessName;
                        String guessDesc = null;
                        String guessImg = null;
                        try {
                            guessName = guess.getName();
                        } catch (Exception ignored) {
                            continue;
                        }
                        try {
                            guessDesc = guess.getDescription();
                        } catch (Exception ignored) {

                        }
                        try {
                            guessImg = guess.getImage().toString();
                        } catch (Exception ignored) {

                        }
                        EmbedBuilder embedBuilder = new EmbedBuilder();

                        if (guessName != null && !guessName.isEmpty()) {
                            embedBuilder.setAuthor(guess.getName() + " ?", "", "");
                        } else {
                            continue;
                        }
                        if (guessDesc != null && !guessDesc.isEmpty()) {
                            embedBuilder.addField("Description", guessDesc);
                        }
                        if (guessImg != null && !guessImg.isEmpty()) {
                            embedBuilder.setImage(guessImg);
                        }
                        textChannel.sendMessage(embedBuilder).join();
                        answered.set(false);


                        //Waiting for the answer
                        listenerManagerAtomicReference.set(textChannel.addMessageCreateListener(event -> {
                            if (event.getMessage().getUserAuthor().get().getId() == user.getId()) {
                                String content = event.getMessage().getContent().toLowerCase();
                                if (content.equalsIgnoreCase("yes")
                                        || content.equalsIgnoreCase("stop")
                                        || content.equalsIgnoreCase("no")
                                        || content.equalsIgnoreCase("oui")
                                        || content.equalsIgnoreCase("non")
                                        || content.equalsIgnoreCase("y")
                                        || content.equalsIgnoreCase("n")
                                        || content.equalsIgnoreCase("s")
                                        || content.equalsIgnoreCase("sim")
                                        || content.equalsIgnoreCase("si")
                                        || content.equalsIgnoreCase("nao")
                                        ) {
                                    switch (content) {
                                        case "y":
                                        case "yes":
                                        case "oui":
                                        case "si":
                                        case "sim":
                                        case "s":
                                            textChannel.sendMessage("Akinator won!").join();
                                            next.set(false);
                                            answered.set(true);
                                            break;
                                        case "n":
                                        case "no":
                                        case "non":
                                        case "nao":
                                            guessList.add(guess.getName());
                                            answered.set(true);
                                            break;
                                        case "stop":
                                            next.set(false);
                                            answered.set(true);
                                            break;
                                    }
                                    listenerManagerAtomicReference.get().remove();
                                }

                            }
                        }).removeAfter(30, TimeUnit.SECONDS));
                        listenerManagerAtomicReference.get().addRemoveHandler(() -> answered.set(true));
                        while (answered.isNot()) {
                            Thread.onSpinWait();
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    textChannel.sendMessage("Something went wrong...");
                    using.remove(user);
                    return;
                }
                textChannel.sendMessage("You won!");
                break;
            }
        }
        //After the game ended
        using.remove(user);
    }

    private Akiwrapper.Answer getAnswer(String toCheck) {
        if (toCheck.equalsIgnoreCase("yes")
                || toCheck.equalsIgnoreCase("no")
                || toCheck.equalsIgnoreCase("oui")
                || toCheck.equalsIgnoreCase("non")
                || toCheck.equalsIgnoreCase("y")
                || toCheck.equalsIgnoreCase("n")
                || toCheck.equalsIgnoreCase("s")
                || toCheck.equalsIgnoreCase("sim")
                || toCheck.equalsIgnoreCase("si")
                || toCheck.equalsIgnoreCase("nao")
                || toCheck.equalsIgnoreCase("i don't know")
                || toCheck.equalsIgnoreCase("i dont know")
                || toCheck.equalsIgnoreCase("i")
                || toCheck.equalsIgnoreCase("idk")
                || toCheck.equalsIgnoreCase("j")
                || toCheck.equalsIgnoreCase("jsp")
                ) {
            switch (toCheck) {
                case "oui":
                case "yes":
                case "y":
                case "si":
                case "sim":
                case "s":
                    return Akiwrapper.Answer.YES;
                case "no":
                case "non":
                case "n":
                case "nao":
                    return Akiwrapper.Answer.NO;
                case "i don't know":
                case "i dont know":
                case "i":
                case "idk":
                case "j":
                case "jsp":
                    return Akiwrapper.Answer.DONT_KNOW;
            }
        }
        else if(toCheck.contains("pro") || toCheck.contains("p")) {
            if (toCheck.contains("n")) {
                return Akiwrapper.Answer.PROBABLY_NOT;
            }
            else {
                return Akiwrapper.Answer.PROBABLY;
            }
        }
        return null;
    }

    private AkiwrapperBuilder setLanguage(String language) {
        switch (language) {
            case "fr":
                return new AkiwrapperBuilder().setLocalization(Server.Language.FRENCH);
            case "nl":
                return new AkiwrapperBuilder().setLocalization(Server.Language.DUTCH);
            case "pt":
                return new AkiwrapperBuilder().setLocalization(Server.Language.PORTUGUESE);
            case "en":
            default:
                return new AkiwrapperBuilder().setLocalization(Server.Language.ENGLISH);
        }
    }
}