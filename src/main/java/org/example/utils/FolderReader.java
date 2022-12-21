package org.example.utils;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


/**
 * klasse med statiske metoder som leser filer
 */
public class FolderReader {
    private static final String IMG_PATH = ".\\src\\main\\resources\\images\\";
    private static final String COMMANDS_PATH = ".\\src\\main\\resources\\";

    /**
     * funksjon som henter bilder fra mappe
     * @param mappe mappenavn
     * @return retunerer en liste med File
     */
    public static File[] getImageFiles(String mappe){

        // prøver om fil fungerer
        try{
            File folder = new File(IMG_PATH +mappe+"");
            File[] listOfFiles = folder.listFiles();
            return listOfFiles;
        } catch (NullPointerException e){
            System.out.println("kunne ikke finne fil: " + e.getMessage());
        }
        return null;
    }

    /**
     * skanner en fil for commands
     * @return returnerer en ArrayList av CommandData
     */
    public static ArrayList<CommandData> getCommandsFromFile() {
        ArrayList<CommandData> commandDataList = new ArrayList<>();

        String fileString = COMMANDS_PATH + "slashcommands.txt";
        // åpner og lukker scanner
        try (Scanner sc = new Scanner(new File(fileString))) {
            sc.nextLine();  // hopp over første linje

            // leser fil linje for linje
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] linjeList = line.split(";");
                commandDataList.add(Commands.slash(linjeList[0], linjeList[1]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("kunne ikke finne fil: " + e.getMessage());
        }

        return commandDataList;
    }

    /**
     * henter ulike responsmeldinger fra fil
     * @return returnerer et hashmap med alle mulige responser
     */
    public static HashMap<String, String[]> getResponsesFromFile() {
        HashMap<String, String[]> responseMap = new HashMap<>();

        String fileString = COMMANDS_PATH + "responsecommands.txt";

        // åpner og lukker scanner
        try (Scanner sc = new Scanner(new File(fileString))) {
            sc.nextLine();  // skip the first line

            // leser fil linje for linje
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] kvSplit = line.split(";");
                String[] listeSplit = kvSplit[1].split(",");
                responseMap.put(kvSplit[0], listeSplit);
            }
        } catch (FileNotFoundException e) {
            System.out.println("kunne ikke finne fil: " + e.getMessage());
        }

        return responseMap;
    }

}
