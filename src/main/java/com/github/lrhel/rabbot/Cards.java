package com.github.lrhel.rabbot;

import com.vdurmont.emoji.EmojiParser;

import java.util.Collections;
import java.util.Stack;

public class Cards {
    public static final int CORAZON = 0;
    public static final int PIKA = 1;
    public static final int TREBOL = 2;
    public static final int DIAMANTE = 3;

    private Stack<Card> deck;

    public Cards() {
        deck = new Stack<>();
        for(int i = 1; i <= 13; i++)
            this.deck.add(new Card(i, CORAZON));
        for (int i = 1; i <= 13; i++)
            this.deck.add(new Card(i, PIKA));
        for (int i = 1; i <= 13; i++)
            this.deck.add(new Card(i, TREBOL));
        for (int i = 1; i <= 13; i++)
            this.deck.add(new Card(i, DIAMANTE));
        this.shuffle();
    }

    public void shuffle() {
        Collections.shuffle(this.deck);
    }

    public Card draw() {
        return this.deck.pop();
    }

    public class Card {
        private int symbole;
        private int value;

        public Card(int value, int symbole) {
            this.setSymbole(symbole);
            this.setValue(value);
        }

        public int getSymbole() {
            return symbole;
        }

        public String getSymboleEmoji(){
            switch (this.getSymbole()) {
                case CORAZON:
                    return EmojiParser.parseToUnicode(":hearts:");
                case PIKA:
                    return EmojiParser.parseToUnicode(":spades:");
                case TREBOL:
                    return EmojiParser.parseToUnicode(":clubs:");
                case DIAMANTE:
                    return EmojiParser.parseToUnicode(":diamonds:");
                default:
                    return "";
            }
        }

        public int getValue() {
            return value;
        }

        private void setSymbole(int symbole) {
            this.symbole = symbole;
        }

        private void setValue(int value) {
            this.value = value;
        }

        public String toString(){
            String string = "";
            switch (this.getValue()) {
                case 11:
                    string += "J";
                    break;
                case 12:
                    string += "D";
                    break;
                case 13:
                    string += "K";
                    break;
                default:
                    string += this.getValue();
                    break;
            }
            return string + " " + this.getSymboleEmoji();
        }
    }
}
