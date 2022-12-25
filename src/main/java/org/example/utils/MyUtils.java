package org.example.utils;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;

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
            if (liste[1].equals("all"))
                return -2;
            return Integer.parseInt(liste[1]);
        } catch (NumberFormatException e) {
            return -1;
        } catch (ArrayIndexOutOfBoundsException e){
            return -1;
        }
    }

    /**
     * sjekker om event ikke kommer fra bruker eller ikke kommer fra guilt
     * @param event event som blir sjekket
     * @return returnerer false om bruker er bot eller event ikke er fra guild
     */
    public static boolean ifBotOrNotFromGuild(MessageReceivedEvent event){
        return ((event.getAuthor().isBot() || !event.isFromGuild()));
    }

    /**
     * sjekker om event ikke kommer fra bruker eller ikke kommer fra guilt
     * @param event event som blir sjekket
     * @return returnerer false om bruker er bot eller event ikke er fra guild
     */
    public static boolean ifBotOrNotFromGuild(UserActivityStartEvent event){
        return ((event.getUser().isBot() || !event.getGuild().isMember(event.getMember())));

    }

    /**
     * sjekker om event ikke kommer fra bruker eller ikke kommer fra guilt
     * @param event event som blir sjekket
     * @return returnerer false om bruker er bot eller event ikke er fra guild
     */
    public static boolean ifBotOrNotFromGuild(SlashCommandInteractionEvent event){
        return ((event.getUser().isBot() || !event.getGuild().isMember(event.getMember())));
    }
}
