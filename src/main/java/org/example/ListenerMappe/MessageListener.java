package org.example.ListenerMappe;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.Main;

import java.util.HashMap;

import static org.example.MyUtilsMappe.FolderReader.getResponsesFromFile;
import static org.example.MyUtilsMappe.MyUtils.getRandomNumber;

/**
 * klasse som lytter etter key-words i meldinger
 */
public class MessageListener extends ListenerAdapter {

    /**
     *  gir beskjed om at listeneren har lastet globalt
     * @param event
     */
    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("message listener klar!");
    }

    /**
     * lytter på om melding blir sendt i discord
     * @param event objekt som fyrer melding sendt
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();

        switch (message){
            case ("test"):
                testFunksjon(event);
                 break;
            case ("takk gud"):
                event.getChannel().sendMessage("takk meg").queue();
                break;
        }
    }

    /**
     * sender melding på kodeord "test"
     * @param event objekt som fyrte testmelding
     */
    public void testFunksjon(MessageReceivedEvent event){
        // prøver å hente verdi fra env fil
        try {
            String myUserid = Main.config.get("MY_USERID");
            String eventUserID = event.getMember().getUser().getId();

            // sender valgt ut bruker melding = vanlig svar, ellers tullesvar
            // kan være variant av ting, vendes om etc etter eget ønske
            String key = "listeTroll";
            if (myUserid.equals(eventUserID))
                key = "listeNormal";
            event.getChannel().sendMessage(getRandomStringFromMap(key)).queue();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * henter en melding som skal bli sendt discord
     * @param key nøkkelord for hvilken liste
     * @return string av respons
     */
    public String getRandomStringFromMap(String key){
        HashMap<String, String[]> responseMap = getResponsesFromFile();
        String[] liste = responseMap.get(key);
        int i = getRandomNumber(0, liste.length-1);
        return liste[i];
    }
}
