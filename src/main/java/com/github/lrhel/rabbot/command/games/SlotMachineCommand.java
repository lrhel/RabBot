package com.github.lrhel.rabbot.command.games;

import com.github.lrhel.rabbot.Money;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.github.lrhel.rabbot.utility.Utility.getMessageDeleter;

public class SlotMachineCommand implements CommandExecutor {
    private static String[] array =  {
            ":game_die:", ":trophy:", ":slot_machine:", ":rabbit:", ":zap:",
            ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:",
            ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:",
            ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:",
            ":game_die:", ":trophy:", ":slot_machine:", ":rabbit:", ":zap:",
            ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:",
            ":frog:", ":cow:", ":frog:", ":cow:", ":frog:", ":cow:", ":frog:", ":cow:",
            ":game_die:", ":trophy:", ":slot_machine:", ":rabbit:", ":zap:",
            ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:",
            ":frog:", ":cow:", ":frog:", ":cow:", ":frog:", ":cow:", ":frog:", ":cow:",
            ":game_die:", ":trophy:", ":slot_machine:", ":rabbit:", ":zap:",
            ":seven:", ":seven:",
            ":game_die:", ":trophy:", ":slot_machine:", ":rabbit:", ":zap:",
            ":game_die:", ":trophy:", ":slot_machine:", ":rabbit:", ":zap:",
            ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:",
            ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:",
            ":game_die:", ":trophy:", ":slot_machine:", ":rabbit:", ":zap:",
            ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:",
            ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:",
            ":game_die:", ":trophy:", ":slot_machine:", ":rabbit:", ":zap:",
            ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:",
            ":frog:", ":cow:", ":frog:", ":cow:", ":frog:", ":cow:", ":frog:", ":cow:",
            ":frog:", ":cow:", ":frog:", ":cow:", ":frog:", ":cow:", ":frog:", ":cow:",
            ":seven:", ":seven:",
            ":game_die:", ":trophy:", ":slot_machine:", ":rabbit:", ":zap:",
            ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:",
            ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:", ":zap:",
            ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:",
            ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:",
            ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:", ":apple:", ":pineapple:",
            ":frog:", ":cow:", ":frog:", ":cow:", ":frog:", ":cow:", ":frog:", ":cow:",
            ":frog:", ":cow:", ":frog:", ":cow:", ":frog:", ":cow:", ":frog:", ":cow:",
            ":seven:", ":seven:"
    };

    private static ArrayList<User> isPlaying = new ArrayList<>();

    @Command(aliases = {"slotmachine", "slot", "spin", "slots"}, description = "Slotmachine 7 7 7", async = true)
    public String onSlotMachineCommand(User user, TextChannel textChannel, String[] arg) {
        int amount;
        int option;

        if(isPlaying.contains(user)) {
            return "";
        }

        if(arg.length > 2) {
            return showHelp();
        }
        if(arg.length > 0 && arg[0].equalsIgnoreCase("help")) {
            return showHelp();
        }

        try {
            amount = Integer.parseInt(arg[0]);
        } catch (Exception ignored) {
            amount = 1;
        }

        try {
            option = Integer.parseInt(arg[1]);
            if(option > 3) {
                option = 3;
            }
            else if (option < 1) {
                option = 1;
            }
        } catch (Exception ignored) {
            option = 1;
        }

        if(Money.getMoney(user) < amount * option) {
            textChannel.sendMessage("Not enough money. . .").thenAccept(getMessageDeleter(5, TimeUnit.SECONDS));
            return "";
        }

        isPlaying.add(user);

        Money.removeMoney(user, amount * option);

        SlotMachine slotMachine = new SlotMachine();

        MessageBuilder messageBuilder;
        messageBuilder = new MessageBuilder();
        messageBuilder.append("[**Slot Machine**]\n");

        messageBuilder.append(slotMachine);

        Message message = textChannel.sendMessage(messageBuilder.getStringBuilder().toString()).join();

        try {
            Thread.sleep(750);
        } catch (Exception ignored) { }

        slotMachine.spin();

        messageBuilder = new MessageBuilder();
        messageBuilder.append("[**Slot Machine**]\n");

        messageBuilder.append(slotMachine);


        message.edit(messageBuilder.getStringBuilder().toString()).join();

        try {
            Thread.sleep(750);
        } catch (Exception ignored) { }

        slotMachine.spin();

        messageBuilder = new MessageBuilder();
        messageBuilder.append("[**Slot Machine**]\n");

        messageBuilder.append(slotMachine);


        message.edit(messageBuilder.getStringBuilder().toString()).join();

        int win = slotMachine.win(option);
        if(win > 0) {
            messageBuilder.append("\nYou have won **" + amount * win + "$**");
        }
        else {
            messageBuilder.append("You have lost **" + amount + "$**");
        }

        Money.addMoney(user, amount * win);

        message.edit(messageBuilder.getStringBuilder().toString()).join();

        isPlaying.remove(user);

        return "";

    }

    private String showHelp() {
        return "**Usage: ** ```rb.slotmachine [amount] [option]"
                + "\nOption:\n" +
                " * 1 : Line 2 (Default)\n" +
                " * 2 : Line 1 & 2 & 3\n" +
                " * 3 : Line 1 & 2 & 3 & Diagonales\n```"
                ;
    }

    private class SlotMachine {
        private ArrayList<String> band1;
        private ArrayList<String> band2;
        private ArrayList<String> band3;
        private int pointerBand1;
        private int pointerBand2;
        private int pointerBand3;

        private Random rng;

        public SlotMachine() {
            this.band1 = new ArrayList<>(10);
            this.band2 = new ArrayList<>(10);
            this.band3 = new ArrayList<>(10);

            this.rng = new Random(System.currentTimeMillis());

            fillBand(band1);
            fillBand(band2);
            fillBand(band3);

            pointerBand1 = rng.nextInt(10);
            pointerBand2 = rng.nextInt(10);
            pointerBand3 = rng.nextInt(10);
        }

        private void fillBand(ArrayList<String> band) {
            for(int i = 0; i < 10; i++) {
                band.add(i, array[rng.nextInt(array.length)]);
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("==========\n\n");

            sb.append(printBand(band1, pointerBand1));
            sb.append("\n\n");
            sb.append(printBand(band2, pointerBand2));
            sb.append("\n\n");
            sb.append(printBand(band3, pointerBand3));
            sb.append("\n\n");
            sb.append("==========\n");

            return sb.toString();
        }

        public void spin() {
            this.pointerBand1 = this.rng.nextInt(10);
            this.pointerBand2 = this.rng.nextInt(10);
            this.pointerBand3 = this.rng.nextInt(10);
        }

        /**
         *
         * @param option 1 = only line 2
         *               2 = line 1 2 3
         *               3 = line 1 2 3 + diagonal
         * @return multiplicator of the win
         */
        public int win(int option) {
            int multiplicator = 0;
            switch (option) {
                case 3:
                    if(band1.get(pointerBand1).equalsIgnoreCase(band2.get((pointerBand2 + 1) % 10))
                            && band2.get((pointerBand1 + 1) % 10).equalsIgnoreCase(band3.get((pointerBand3 + 2) % 10))
                    ) {
                        multiplicator += gain(band1.get(pointerBand1));
                    }
                    else if (band3.get(pointerBand3).equalsIgnoreCase(band2.get((pointerBand2 + 1) % 10))
                            && band2.get((pointerBand2 + 1) % 10).equalsIgnoreCase(band1.get((pointerBand1 + 2) % 10))
                    ) {
                        multiplicator += gain(band3.get(pointerBand3));
                    }
                case 2:
                    if(band1.get(pointerBand1).equalsIgnoreCase(band1.get((pointerBand1 + 1) % 10))
                            && band1.get(pointerBand1).equalsIgnoreCase(band1.get((pointerBand1 + 2) % 10))
                    ) {
                        multiplicator += gain(band1.get(pointerBand1));
                    }
                    else if(band3.get(pointerBand3).equalsIgnoreCase(band3.get((pointerBand3 + 1) % 10))
                            && band3.get(pointerBand3).equalsIgnoreCase(band3.get((pointerBand3 + 2) % 10))
                    ) {
                        multiplicator += gain(band3.get(pointerBand3));
                    }
                case 1:
                    if(band2.get(pointerBand2).equalsIgnoreCase(band2.get((pointerBand2 + 1) % 10))
                            && band2.get(pointerBand2).equalsIgnoreCase(band2.get((pointerBand2 + 2) % 10))
                    ) {
                        multiplicator += gain(band2.get(pointerBand2));
                    }
                default:
                    return multiplicator;
            }
        }

        private int gain(String str) {
            switch (str) {
                case ":rabbit:":
                    return 1000;
                case ":game_die:":
                case ":trophy:":
                case ":slot_machine:":
                    return 500;
                case ":zap:":
                    return 1;
                case ":apple:":
                case ":pineapple:":
                    return 10;
                case ":frog:":
                case ":cow:":
                    return 50;
                case ":seven:":
                    return 777;
                default:
                    return 0;

            }
        }

        private String printBand(ArrayList<String> band, int pointerBand) {
            StringBuilder sb = new StringBuilder();
            sb.append(band.get(pointerBand));
            sb.append(" : ");
            sb.append(band.get(++pointerBand % 10));
            sb.append(" : ");
            sb.append(band.get(++pointerBand % 10));
            return sb.toString();
        }
    }
}
