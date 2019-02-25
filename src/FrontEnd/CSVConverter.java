package FrontEnd;

import api.Species;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;


public class CSVConverter {

    private static String columnNumber(int n) {
        return new String(new char[n-1]).replace("\0", "%s,") + "%s\n";
    }

    private static void writeToCSV(String plantName, File path) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(path, true))) {
            DataCollected dataCollected = new DataCollected();
            List<Species> toPrint = dataCollected.dataToPrint(plantName);

            writer.printf(columnNumber(8),
                    "Scientific Name", "Species", "Genus", "Family", "BHcode", "FEcode", "Accepted?", "Basionym?");

            for (Species sp : toPrint) {
                writer.printf(columnNumber(8),
                        sp.getCanonicalName(), sp.getSpecies(), sp.getGenus(), sp.getFamily(), sp.getBHcode(),
                        sp.getFEcode(), sp.isSynonym() ? " " : "X", sp.isBasionym() ? "X" : " ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void exportCSV(String query, File path){
        exportCSVMultiple(Arrays.asList(query), path);
    }

    public static void exportCSVMultiple(List<String> queries, File path){
        path.delete();
        for(String query : queries){
            writeToCSV(query, path);
        }
    }

}
