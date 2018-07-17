package com.github.lrhel.rabbot;

import java.util.Random;

public class Roulette {

    public static int GREEN = 0;
    public static int RED = 1;
    public static int BLACK = 2;

    private int[] wheel;
    private Random rng;

    private Roulette() {
        wheel = new int[37];
        for(int i = 1; i <= 9; i++){
            if(i % 2 == 0)
                wheel[i] = BLACK; //pair
            else
                wheel[i] = RED; //impair
        }
        wheel[10] = BLACK;
        for(int i = 11; i <= 18; i++){
            if(i % 2 == 0)
                wheel[i] = RED; //pair
            else
                wheel[i] = BLACK; //impair
        }
        wheel[19] = BLACK;
        for(int i = 20; i <= 26; i++){
            if(i % 2 == 0)
                wheel[i] = BLACK; //pair
            else
                wheel[i] = RED; //impair
        }
        wheel[27] = RED;
        for(int i = 28; i <= 36; i++){
            if(i % 2 == 0)
                wheel[i] = RED;
            else
                wheel[i] = BLACK;
        }
        wheel[0] = GREEN;
        rng = new Random(System.currentTimeMillis());
    }

    private static class RouletteHolder {
        private final static Roulette instance = new Roulette();
    }

    public static Roulette getInstance() {
        return RouletteHolder.instance;
    }

    public String spin() {
        int spin = rng.nextInt(37);
        if(spin == 0)
            return "0 GREEN";
        String color = wheel[spin] == RED ? "RED" : "BLACK";
        String odd = spin % 2 == 0 ? "EVEN" : "ODD";
        return spin + " " + color + " " + odd;
    }
}
