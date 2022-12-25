package org.example.games;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import org.example.database.UserHandler;
import org.example.utils.MyUtils;

/**
 * klasse for å legge ut en tilfeldig skatt på serveren
 */
public class KapteinSkatt {
    private static int SKATT = 0;

    /**
     * funksjon som trigges for hver melding som blir sendt
     * @param event event trigget fra melding
     */
    public static void muligSkatt(MessageReceivedEvent event){


        int releaseSkatt = MyUtils.getRandomNumber(1, 20);
        // er tall 1? sjanse 1-20 for at det blir skatt
        if (releaseSkatt == 1){

            // tilfeldig sum på skatten og oppdaterer skatten
            int randomSkatt = MyUtils.getRandomNumber(1, 500);
            SKATT += randomSkatt;

            // sender melding om at skatt er tilgjengelig
            String skattemelding = "kaptein snabelsatan har forlist skuta! " + SKATT + " høvdinger flyter rundt på havet";
            event.getChannel().sendMessage(skattemelding).queue();
        }
    }

    /**
     * funskjon som lar bruker hente skatt om tilgjengelig
     * @param userID bruker som henter skatten
     * @param event event som har blitt trigget av !hentSKatt
     */
    public static void claimSkatt(String userID, MessageReceivedEvent event) {
        // er skatt 0 så er det ikke mulig å hente
        if (SKATT == 0){
            event.getChannel().sendMessage("ingen skatt for øyeblikket").queue();
            return;
        }
        // prøver å oppdatere balance til bruker med balance+SKATT
        try {
            boolean sukess = UserHandler.updateBalance(userID, UserHandler.getBalance(userID)+SKATT);

            // hvis suksess oppdater skatt og send melding
            if (sukess){
                SKATT = 0;
                event.getChannel().sendMessage("skatten er sanket inn hiv og hoi").queue();
            }
            // feilmelding
            else {
                event.getChannel().sendMessage("noe gikk galt").queue();
            }
        } catch (Exception e) {
            event.getChannel().sendMessage("en feil has skjedd: " + e.getMessage()).queue();
        }
    }
}
