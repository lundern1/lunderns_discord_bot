package org.example.MyUtilsMappe;

import java.util.Random;

/**
 * klasse med ulike generelle hjelpefunksjoner som trengs
 */
public class MyUtils {


    /**
     * få et tilfeldig nummer
     * @param min minste verdi
     * @param max høyest verdi
     * @return int av tilfeldig tall mellom min og max
     */
    public static int getRandomNumber(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
