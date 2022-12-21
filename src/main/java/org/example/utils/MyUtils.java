package org.example.utils;

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
    /**
     * funksjon som sjekker om bruker har gitt
     * gyldig beløp å gamble med
     * @param message String melding fra bruker
     * @return int returnerer beløp bruker vil spille for, -1 om ugyldig beløp
     */
    public static int getSumFromString(String message) {

        try{
            String[] liste = message.split(" ");
            return Integer.parseInt(liste[1]);
        } catch (NumberFormatException e) {
            return -1;
        } catch (ArrayIndexOutOfBoundsException e){
            return -1;
        }
    }
}
