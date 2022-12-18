package org.example.MyUtilsMappe;

import java.io.File;

public class FolderReader {
    public static File[] getFiles(String mappe){
        File folder = new File(".\\src\\main\\resources\\images\\"+mappe+"");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }
        return listOfFiles;
    }

}
